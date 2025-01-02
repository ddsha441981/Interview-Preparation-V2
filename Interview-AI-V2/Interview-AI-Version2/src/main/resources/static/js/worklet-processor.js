class AudioProcessor extends AudioWorkletProcessor {
    process(inputs, outputs, parameters) {
        const input = inputs[0];
        const output = outputs[0];
        if (input && input.length > 0) {
            const inputChannel = input[0];
            const outputChannel = output[0];
            for (let i = 0; i < inputChannel.length; i++) {
                outputChannel[i] = inputChannel[i];
            }
            this.port.postMessage(inputChannel); // Send audio data to the main thread
        }
        return true;
    }
}

registerProcessor('processor', AudioProcessor);
