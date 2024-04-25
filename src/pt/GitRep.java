/**
*
* @author Ali Kutay Kılınç
* @since 01.04.2024
* Kullanıcıdan alınan URL ile yerel dizine klonlama işlemini yapar
* Klonlama yapılacak klasör daha önce oluşturulmuş ise yeni klonlama öncesi silinir
*/
package pt;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Scanner;

public class GitRep {

	// Dosyayı içindekilerle birlikte tamamen siler. İçinin boş olma zorunluluğu yok
	void recursiveDelete(File file) {
        if (!file.exists())
            return;
        if (file.isDirectory()) {
            for (File f : file.listFiles()) {
                recursiveDelete(f);
            }
        }
        file.delete();
    }
	// Alınan GitHub linki Git ile projeyi bu kodun çalıştığı klasör içinde "repository" klasörüne klonlar
	public String CloneProcess(){
		// Kullanıcıdan GitHub URL alınır
		Scanner scanner = new Scanner(System.in);
		String url = scanner.nextLine(); 
	    // Projenin çalıştığı dosya konumu bulunur
	    String projectDirectory = Paths.get("").toAbsolutePath().toString();
	    // Klonlamak için projedeki klasör içinde "repository" dizinini belirler
	    String localPath = Paths.get(projectDirectory, "repository").toString();
	    
	    
	    File folderToDelete = new File(localPath);
	    // Dosya daha önce oluşturulmuş mu kontrolü
        // "repository" klasörü daha önce oluşturulmuş ve kod tekrar çalışmışsa daha önce oluşan klasör tüm içeriğiyle tamamen silinir
	    if (folderToDelete.exists()) {
	    	recursiveDelete(folderToDelete);
	    	System.out.println("repository '" + folderToDelete + "' daha önce olusturulmus. Yenisi icin silindi");
	    }
	    
	    // "repository" klasörünü oluşturur
	    folderToDelete.mkdirs();
	    System.out.println("repository klasoru '" + folderToDelete + " olusturuldu");

	    // git deposunu klonlar
	    try {
	      ProcessBuilder builder = new ProcessBuilder("git", "clone", url, localPath);
	      Process process = builder.start();
	      int exitCode = process.waitFor();

	      if (exitCode == 0) {
	        System.out.println("Git Repository klonlandi " + localPath);
	      } else {
	        System.out.println("Klonlanma basarısız exit code: " + exitCode);
	      }
	    } catch (IOException | InterruptedException e) {
	      System.out.println("Error: " + e.getMessage());
	    }

	    scanner.close();
	    return localPath;
	    }
}

