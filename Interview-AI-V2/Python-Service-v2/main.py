import asyncio
import websockets
import whisper
import os
import openai
import requests
import json

# Set your OpenAI and Google Gemini API keys here
openai.api_key = '<YOUR-API_KEY>'
google_gemini_api_key = '<YOUR-API_KEY>'

async def handle_audio(websocket, path):
    audio_data = b''
    async for message in websocket:
        audio_data += message
        print(f"Received audio data: {len(message)} bytes")

    # Save the received audio data to a file
    audio_file_path = "temp_audio.wav"
    with open(audio_file_path, "wb") as f:
        f.write(audio_data)
    print(f"Audio saved to {audio_file_path}")

    # Load Whisper model
    model = whisper.load_model("base")  # Adjust the model size as needed

    # Transcribe audio
    result = model.transcribe(audio_file_path)
    transcription = result['text']
    print("Transcription result:", transcription)

    # Send the transcription to OpenAI and Google Gemini and get their responses
    openai_response = await send_to_openai(transcription)
    google_gemini_response = send_to_google_gemini(transcription)

    print("OpenAI response:", openai_response)
    print("Google Gemini response:", google_gemini_response)

    # Optionally, remove the temporary audio file
    os.remove(audio_file_path)

async def send_to_openai(transcription):
    try:
        response = await openai.ChatCompletion.create(
            model="gpt-3.5-turbo",  # Use the desired model
            messages=[
                {"role": "user", "content": transcription}
            ]
        )
        return response.choices[0].message['content']
    except Exception as e:
        print(f"Error communicating with OpenAI: {e}")
        return None
def send_to_google_gemini(transcription):
    try:
        url = f"https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash-latest:generateContent?key={google_gemini_api_key}"
        headers = {
            "Content-Type": "application/json"
        }
        
        # Building the payload dynamically
        payload = {
            "contents": [
                {"parts": [{"text": transcription}]}
            ]
        }

        # Convert the payload to a JSON string
        payload_json = json.dumps(payload)

        # Send the POST request to Google Gemini API
        response = requests.post(url, headers=headers, data=payload_json)
        response.raise_for_status()

        # Print the entire response for debugging
        response_json = response.json()
        print("Google Gemini full response:", response_json)

        # Attempt to retrieve the 'candidates' field
        return response_json.get('candidates', [{}])[0].get('output', 'No output found in response')
    except Exception as e:
        print(f"Error communicating with Google Gemini: {e}")
        return None


start_server = websockets.serve(handle_audio, "localhost", 5000)

print("WebSocket server starting on ws://localhost:5000/audio")
asyncio.get_event_loop().run_until_complete(start_server)
asyncio.get_event_loop().run_forever()

