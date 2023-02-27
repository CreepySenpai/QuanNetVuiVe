package connect;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;

public class SocketLib {
private Socket socket;
	
	private JTextArea khungChat;
	
	private PrintWriter out;
	
	private BufferedReader reader;
	
	public SocketLib(Socket socket, JTextArea khungChat) throws IOException {
		this.socket = socket;
		this.khungChat = khungChat;
		
		out = new PrintWriter(socket.getOutputStream());
		
		reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		
		Receive();
	}
	
	public void Receive() {
		Thread thread = new Thread() {
			public void run() {
				while(true) {
					try {
						String lineString = reader.readLine();
						if (lineString != null) {
							khungChat.append(lineString + "\n");
						}
					} catch (Exception e) {
						
					}
				}
			}
		};
		thread.start();
	}
	
	
	public void Send(String message) {
		out.println(message);
		out.flush();
	}
	
	public void CloseConnect() {
		try {
			if ((socket != null) && (out != null) && (reader != null)) {
				socket.close();
				out.close();
				reader.close();
			}
		} catch (Exception e) {
			JOptionPane.showInternalMessageDialog(null, "Error: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	public static void CloseConnect(ServerSocket serverSocket, Socket socket) {
		try {
			serverSocket.close();
			socket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
