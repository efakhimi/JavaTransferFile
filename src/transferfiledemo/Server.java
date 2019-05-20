/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package transferfiledemo;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author 100111010111001
 */
public class Server {
    
    public static void main(String[] args) throws Exception
    {
        String state="start";
        String sourceFileName = "1.png";
        String outputFileName = "2.png";
        
        ServerSocket server = new ServerSocket(2030);
        BufferedReader request;
        DataOutputStream response;
        
        while(true)
        {
            Socket connection = server.accept();
            request = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            response = new DataOutputStream(connection.getOutputStream());
            
            while(state.equals("start"))
            {
                System.out.println("Server : start");
                response.writeBytes("newFile_"+outputFileName+"\n");
                state = request.readLine();
                
                System.out.println("Server : state => "+state);
            }
            
            while(state.equals("upload"))
            {
                System.out.println("Server : upload");
                response.writeBytes("download\n");
                
                File source = new File(sourceFileName);
                
                response.writeBytes((int)source.length()+"\n");
                
                response.flush();
                
                byte[] byteArr = new byte[(int)source.length()];
                System.out.println(source.length());
                FileInputStream fileInpStream = new FileInputStream(source);
                
                BufferedInputStream buffer = new BufferedInputStream(fileInpStream);
                
                buffer.read(byteArr, 0, byteArr.length);
                response.write(byteArr, 0, byteArr.length);
                response.flush();
                
                state = request.readLine();
                if(state.equals("done"))
                    System.out.println("Server : uploaded succesfully");
            }
            
            
            //System.out.println("Server : close Connection");
            connection.close();
        }
        
    }
    
}
