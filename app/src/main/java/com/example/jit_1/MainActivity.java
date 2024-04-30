package com.example.jit_1;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import java.util.Random;


public class MainActivity extends AppCompatActivity {
    private EditText emailEditText;
    private Button sendButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        emailEditText = findViewById(R.id.email_edit_text);
        sendButton = findViewById(R.id.send_button);

        sendButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString().trim();
            if (!email.isEmpty()) {
                sendVerificationCode(email);
            } else {
                Toast.makeText(MainActivity.this, R.string.enter_email_address_message, Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void sendVerificationCode(String email) {

        String verificationCode = generateVerificationCode();


        final String senderEmail = "examplemail@gmail.com";
        final String senderPassword = "*****";


        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");


        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, senderPassword);
            }
        });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(senderEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
            message.setSubject("Verification Code");
            message.setText("Your verification code is: " + verificationCode);
            Transport.send(message);

            Toast.makeText(MainActivity.this, getString(R.string.verification_code_sent_message, email), Toast.LENGTH_SHORT).show();
        } catch (MessagingException e) {

            e.printStackTrace();
            Toast.makeText(MainActivity.this, R.string.failed_to_send_verification_code_message, Toast.LENGTH_SHORT).show();
        }
    }


    private String generateVerificationCode() {

        Random random = new Random();
        int code = 100000 + random.nextInt(900000);
        return String.valueOf(code);
    }
}
