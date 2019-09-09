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
    public int BUFSIZE = 1024;

    public void Clicked(ActionEvent event) {
        String result = "接続中";
        connect.setText(result);
        String ip = ip_ad.getText();
        int port = Integer.parseInt(port_num.getText());
        System.out.println(ip);
        System.out.println(port);
        try {
            socket = new Socket(ip, port);
            InThread inThread = new InThread(socket);
            inThread.start();
        } catch (IOException e) {
            System.out.println(e);
        }
    }
    class InThread extends Thread{
        private Socket socket;
        private InputStream in;
        public int totalBytesRcvd ;
        public int bytesRcvd;
        public InThread(Socket socket){
            this.socket = socket;
            try{
                this.in = socket.getInputStream();
                GetThread getThread = new GetThread(in);
                getThread.start();
//                while (totalBytesRcvd < 2) {
//                    if ((bytesRcvd = in.read( // 引数は読込データ、Offset、読込データ長
//                            msg,
//                            totalBytesRcvd,
//                            2 - totalBytesRcvd)) == -1) {
//                        break;
//                    }
//                    totalBytesRcvd += bytesRcvd;
//                }
            }catch (IOException e) {
                System.out.println(e);
            }
        }
    }

    class GetThread extends Thread{
        private InputStream in;
        public GetThread(InputStream inputStream){
            this.in = inputStream;
        }
        public void run(){
            try{
                int totalBytesRcvd;
                byte[] msg = new byte[1024];
                byte[] bytenum;
                while(true) {
                    bytenum = new byte[1];
                    in.read(bytenum, 0, 1);
                    String aa = new String(bytenum);
                    int num = Integer.parseInt(aa);
                    System.out.println(num);
                    byte[] receiveBuf = new byte[num];
                    totalBytesRcvd = 0;
                    int recvMsgSize;
                    while (true) {
                        recvMsgSize = in.read(receiveBuf);
                        totalBytesRcvd += recvMsgSize;
                        if (totalBytesRcvd == num) {
                            break;
                        }
                    }
                    String m = new String(receiveBuf);
                    String txt = input.getText();
                    input.setText(txt + "\n" + m);
                    System.out.println(m);
                }
            }catch (IOException e) {
                System.out.println(e);
            }

        }
    }

    public void sendClicked(ActionEvent event) {
        try{
            InputStream in = socket.getInputStream();
            OutputStream out = socket.getOutputStream();
            String sData = output.getText();
            byte[] data = sData.getBytes();
            byte[] msg = new byte[1024];
            out.write(data);
            System.out.println("sent");
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public void Finish(ActionEvent event) {
        System.exit(0);
    }


}
