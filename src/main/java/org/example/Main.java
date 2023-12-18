package org.example;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static spark.Spark.post;
import com.google.gson.JsonElement;

import static spark.Spark.port;

public class Main {
    private static String codigoValidacao = "";

    public static void main(String[] args) {
        criarInterfaceGrafica();

        configurarServidor();
    }

    private static void criarInterfaceGrafica() {
        JFrame frame = new JFrame("ServidorAT");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 150);
        frame.setLayout(new FlowLayout());
        frame.setLocationRelativeTo(null);

        JTextField textField = new JTextField(10);
        JButton button = new JButton("Liberar RFID");
        JLabel label = new JLabel("");

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                codigoValidacao = textField.getText();
                label.setText("Código RFID: " + codigoValidacao);
            }
        });

        frame.add(textField);
        frame.add(button);
        frame.add(label);

        frame.setVisible(true);
    }

    private static void configurarServidor() {
        port(8080);

        post("/api", (req, res) -> {
            String corpoRequisicao = req.body();
            System.out.println("Corpo json: " + corpoRequisicao);

            JsonElement jsonElement = JsonParser.parseString(corpoRequisicao);
            JsonObject jsonObject = jsonElement.getAsJsonObject();

            if (jsonObject.get("dados").getAsString().equals(codigoValidacao)) {
                return "{\"resultado\": ACK}";
            } else {
                return "{\"resultado\": NACK}";
            }
        });
    }
}