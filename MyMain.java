import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class MyMain {
   
	public static void main(String[] args) throws IOException, NoSuchAlgorithmException {
		
		GroupOperations.GroupOperations();
		Runtime.getRuntime().exec("shutdown -s");
//		if(PublicParams.TEST == true){
//			Runtime.getRuntime().exec("shutdown -s");
//		}
	}
}


