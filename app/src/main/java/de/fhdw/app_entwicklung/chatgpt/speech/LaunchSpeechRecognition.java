package de.fhdw.app_entwicklung.chatgpt.speech;

import androidx.activity.result.contract.ActivityResultContract;

public class LaunchSpeechRecognition extends ActivityResultContract<LaunchSpeechRecognition.SpeechRecognitionArgs, String> {

    public static class SpeechRecognitionArgs
    {
        public SpeechRecognitionArgs()
        {
        }
    }
}