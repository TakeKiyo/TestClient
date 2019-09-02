package sample;

import com.sun.prism.image.CachingCompoundImage;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import java.net.*;
import java.io.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Controller {
    public Button start;
    public Button send;
    public TextField ip_ad;
    public TextField port_num;
    public Label connect;
    public TextField output;
    public TextArea input;
    Socket socket = null;
    public void Clicked(ActionEvent event) {
        String result = "接続中";
        connect.setText(result);
        String ip = ip_ad.getText();
        int port = Integer.parseInt(port_num.getText());
        System.out.println(ip);
        System.out.println(port);
        try {
            socket = new Socket(ip, port);
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public void sendClicked(ActionEvent event) {
        try{
            String ip = ip_ad.getText();
            int port = Integer.parseInt(port_num.getText());
            Socket socket = new Socket(ip, port);
            String sData = output.getText();
            OutputStream cOut = socket.getOutputStream();
            OutputStreamWriter cOutwriter = new OutputStreamWriter(cOut);
            cOutwriter.write(sData+ "\n");
            cOutwriter.flush();

            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String gotData = reader.readLine();
            input.setText(gotData);
            System.out.println("受信データ:"+gotData);
            System.out.println();
            socket.close();
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public void Finish(ActionEvent event) {
        System.exit(0);
    }


}
