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
                byte[] bytenum;
                input.setText("start");
                while(true) {
//                    bytenum = new byte[1];
//                    in.read(bytenum, 0, 1);
//                    String aa = new String(bytenum);
//                    int num = Integer.parseInt(aa);
//                    System.out.println(num);
//                    byte[] receiveBuf = new byte[num];
//                    totalBytesRcvd = 0;
//                    int recvMsgSize;
//                    System.out.println("OK");
//                    while (true) {
//                        recvMsgSize = in.read(receiveBuf);
//                        totalBytesRcvd += recvMsgSize;
//                        if (totalBytesRcvd == num) {
//                            break;
//                        }
//                    }
                    byte[] msg = new byte[1024];
                    in.read(msg);

//                    String m = new String(receiveBuf);
                    String m = new String(msg);
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
            OutputStream out = socket.getOutputStream();
            String sData = output.getText();
            byte[] first = new byte[1];
            first[0] = 0x02;
            String fi_st = new String(first);

            String bcc = "11";
            byte[] last = new byte[1];
            first[0] = 0x03;
            String la_st = new String(last);



            String data_st = fi_st + sData +bcc+ la_st;
            System.out.println(data_st);
            byte[] data = data_st.getBytes();
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
