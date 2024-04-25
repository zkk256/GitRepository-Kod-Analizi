/**
*
* @author Ali Kutay Kılınç
* @since 01.04.2024
* Main methodu
*/
package pt;

import java.io.IOException;
public class Main {
	public static void main(String[] args) throws IOException {
		
		System.out.print("Repository URL: ");
		GitRep objGitRep =new GitRep();
		
		Analiz objAnaliz= new Analiz();
		String konum= objGitRep.CloneProcess();
		objAnaliz.processSubfolders(konum);
	}

}
