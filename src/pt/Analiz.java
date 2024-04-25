/**
*
* @author Ali Kutay Kılınç
* @since 04.04.2024
* GitRep class'ından dosya konumu değeri alır
* Klonlanmış repository içindeki .java uzantılı class dosyalarını seçer
* Seçilen dosyaları analiz eder
* 
*/
package pt;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Analiz {

	// Yorum sapma yüzdesi Hesaplama
	public double hesaplaYorumSapmaYuzdesi(int countJavadocSatiri,int countYorumSatiri,int countKodSatiri,int countFonksiyonSatiri) {
		
		/*
		 * 		int countJavadocSatiri = 0;
		 * int countYorumSatiri = 0;	// tekliYorumSatiri + cokluYorumSatiri
		 * int countKodSatiri = 0;
		 * int countLoc = 0;
		 * int countFonksiyonSatiri = 0;
		 * double countSapma = 0;
		*/
		
		double YG=(((((double)countJavadocSatiri + (double)countYorumSatiri)*0.8) /(double)countFonksiyonSatiri));
		double YH= (((double)countKodSatiri/(double)countFonksiyonSatiri)*0.3);
		double yorumSapma =(100*YG)/YH-100;
		return yorumSapma;
	}
	public static int konumHesaplaCokluYorum(String line) {
		//1,2,3=Baş, Orta, Son
		String bosluksuzLine = line.stripLeading();
		int x=0;
		// javadoc başlangıcıyla karıştırmamak için
		if (bosluksuzLine.endsWith("/*") && !bosluksuzLine.startsWith("/**")) {
			x=1;
			return x;
		}
		else if (bosluksuzLine.endsWith("*/")) {
			x=3;
			return x;
		}
		else if (bosluksuzLine.startsWith("*")) {
			x=2;
			return x;
		}
		return x;
	}
	public int konumJavadoc(String line) {
		//1,2,3=Baş, Orta, Son
		String bosluksuzLine = line.stripLeading();
		int x=0;
		if (bosluksuzLine.startsWith("/**")) {
			x=1;
			return x;
		}
		else if (bosluksuzLine.endsWith("*/")) {
			x=3;
			return x;
		}
		else if (bosluksuzLine.startsWith("*")) {
			x=2;
			return x;
		}
		return x;
	}	
	//Satır Başı Tekli Yorum Satırı
	public boolean isSatirBasiTekliYorumSatiri(String line) {
		String bosluksuzLine = line.stripLeading();
		if (bosluksuzLine.startsWith("//")) {
			return true;
		}
		return false;	
	}
	//Satır Sonu Tekli Yorum Satırı
	public boolean isSatirSonuTekliYorumSatiri(String line) {
		String bosluksuzLine = line.stripLeading();
		// Satır sonu yorumu alır. 
		if (!bosluksuzLine.startsWith("//") && line.contains("; //")) {
			// Tırnak içindekileri almaz ( "// ) ve ( //" ) Satır başı tekli yorumu almaz
			if (bosluksuzLine.contains("//") && (!bosluksuzLine.contains("\"//") || !bosluksuzLine.contains("//\""))) {
				return true;
			}
			return true;
		}
		return false;	
	}		
	// Fonksiyon başlangıcı mı################################################
	public static boolean isFonksiyonSatiriBasi(String line) {
		  if (line == null || line.isBlank()) {
		    return false;
		  }
		  String trimmedLine = line.strip(); 
		  // Fonksiyon tanımına uymayanları çıkarır
		  if (trimmedLine.contains("class ") || 
		      trimmedLine.startsWith("if ") || 
		      trimmedLine.startsWith("else if ") || 
		      trimmedLine.startsWith("else ") || 
		      trimmedLine.startsWith("for ") || 
		      trimmedLine.startsWith("while ") || 
		      trimmedLine.startsWith("do ")) {
		    return false;
		  }
		  // Var mı (public, private, protected)
		  if (trimmedLine.startsWith("public ") ||
			  trimmedLine.startsWith("private ") ||
			  trimmedLine.startsWith("protected ") ||
			  trimmedLine.startsWith("void ") ||
			  trimmedLine.startsWith("int ") ||
			  trimmedLine.startsWith("double ") ||
			  trimmedLine.startsWith("String ") ||
			  trimmedLine.startsWith("boolean ") ||
			  trimmedLine.startsWith("byte ") ||
			  trimmedLine.startsWith("short ") ||
			  trimmedLine.startsWith("long ") ||
			  trimmedLine.startsWith("float ") ||
			  trimmedLine.startsWith("char ") ||
			  trimmedLine.startsWith("main(")) 
		  {
			  // Aranan semboller: " ( " ve " ) " İstenmeyen: " ; "
			  if (trimmedLine.contains("(") && trimmedLine.contains(")")&& !trimmedLine.contains(";")) {
				return true;
				}  
		  }

		  return false;
		}
	// BoslukSatiri mı?
	public boolean isBoslukSatiri(String line) {
        if (line.isBlank()) {
            return true;
        }
		return false;
	}
	public void satirAnaliz( File file) throws IOException {
		
		int countJavadocSatiri = 0;
		int countYorumSatiri = 0;	// tekliYorumSatiri + cokluYorumSatiri
		int countKodSatiri = 0;
		int countLoc = 0;
		int countFonksiyonSatiri = 0;
		double countSapma = 0;
		
		// Dosyayı satır satır okur
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            
            boolean javadocSatiri = false;
            boolean cokluYorumSatiri = false;	//Diğer yorumlar satırı
            while ((line = br.readLine()) != null) {       		
        		boolean SatirBasiTekliYorumSatiri = false;	// Diğer yorumlar satırı
        		boolean SatirSonuTekliYorumSatiri = false;	// Diğer yorumlar satırı
        		boolean kodSatiri = false;
        		boolean fonksiyonSatiri = false; 
        		boolean boslukSatiri = false;
            	boolean atlaSatiri = false;
            	
            	//javadoc  içinde miyiz kontrolü
            	int konumJavadoc=konumJavadoc(line);
            	
            	switch (konumJavadoc) {
				case 1:
					javadocSatiri=true;
					atlaSatiri = true;
					break;
				case 2:
					if (javadocSatiri) {
						countJavadocSatiri++;
					}
					break;
				case 3:
					javadocSatiri=false;
					atlaSatiri = true;
					break;
				case 0:
					javadocSatiri=false;
					break;
				default:
					throw new IllegalArgumentException("Unexpected value: " + konumJavadoc);
				}            	        	
            	//cokluYorum  içinde miyiz kontrolü
            	int konumCokluYorum=konumHesaplaCokluYorum(line);
            	switch (konumCokluYorum) {
				case 1:
					cokluYorumSatiri=true;
					atlaSatiri = true;
					break;
				case 2:
					if (cokluYorumSatiri) {
						countYorumSatiri++;
					}
					break;
				case 3:
					cokluYorumSatiri=false;
					atlaSatiri = true;
					break;
				case 0:
					cokluYorumSatiri=false;
					break;
				default:
					throw new IllegalArgumentException("Unexpected value: " + konumCokluYorum);
				}
            	
        		//javadocSatiri = isJavadoc(line);
            	SatirSonuTekliYorumSatiri = isSatirSonuTekliYorumSatiri(line);
            	SatirBasiTekliYorumSatiri = isSatirBasiTekliYorumSatiri(line);
        		//cokluYorumSatiri = isCokluYorumSatiri(line);
        		fonksiyonSatiri = isFonksiyonSatiriBasi(line);
        		boslukSatiri = isBoslukSatiri(line); 		
               
                // Boş satır olması durumunda ve
                // sayılmayacak satırsa başka karşılaştırma yapılmaz
                if(boslukSatiri || atlaSatiri) { 
        		} 
                // Javadoc satırı
                else if(javadocSatiri) { 
        		}
                // Kod satırı
                else if(!cokluYorumSatiri && !SatirBasiTekliYorumSatiri) {
            		kodSatiri = true;
                	if(kodSatiri){
                		// Kod sonrası tekli yorum içerir mi
                		if (SatirSonuTekliYorumSatiri) {
            				countYorumSatiri++;
            				}
                		// Kod satırı bir fonksiyon başlangıcı mı?
                		if (fonksiyonSatiri) {
                			countFonksiyonSatiri++;
            				}
                		countKodSatiri++;
                		}
                	}                
                // Çoklu yorum satırı
        		else if(cokluYorumSatiri) {
        			}	
                // Tekli yorum satırı [(kodSatiri && fonksiyonSatiri)=false]
        		else if(SatirBasiTekliYorumSatiri) {
        				countYorumSatiri++;
        			}
                }
        } catch (IOException e) {
            e.printStackTrace();
        }		       
    	// LOC (Line of Code) (Bir dosyadaki her şey dahil satır sayısı)
    	countLoc = countLineOfCode(file);
    	//Yorum Sapma
    	countSapma= hesaplaYorumSapmaYuzdesi(countJavadocSatiri,countYorumSatiri,countKodSatiri,countFonksiyonSatiri);    	
    	// Yorum sapma virgülden sonra iki basamak al
    	double sapma = Math.round(countSapma * 100.0) / 100.0; 
    	
        // Ekrana yazdırma
    	System.out.println("-----------------------------------------");
    	System.out.println("Sınıf: "+file.getName());
        System.out.println("Javadoc Satır Sayısı: " + countJavadocSatiri);
        System.out.println("Yorum Satır Sayısı: " + countYorumSatiri);
        System.out.println("Kod Satır Sayısı: " + countKodSatiri);
    	System.out.println("LOC: " + countLoc); 
        System.out.println("Fonksiyon Sayısı: " + countFonksiyonSatiri);
        System.out.println("Yorum Sapma Yüzdesi: % " + sapma);	
	}
	
	// LOC (Line of Code) (Bir dosyadaki her şey dahil satır sayısı)
	public static int countLineOfCode(File file) throws IOException {
		  // Convert File to Path object
		  Path filePath = file.toPath();
		  return Files.readAllLines(filePath).size();
		}
	
	// İçinde "class" olan ".java" uzantılı dosyalarla işlem yapar
	public void processSubfolders(String path) {
		  // GitRep class'ından string olarak alınacak "localPath" değerini File tipine dönüştürür
		  File dir = new File(path);

		  // repsository klasörünün konumda var olduğunu doğrular
		  if (!dir.exists()) {
		    System.err.println("Error: repsository dosyası bulunamadı " + path);
		    return;
		  }
		  // ".java" ile biten içinde "class" içeren dosyaları seçer. Recursive olarak klasör içindeki klasörlere kadar inceler
		  for (File file : dir.listFiles()) {
		    if (file.isFile() && file.getName().endsWith(".java")) {
		      try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
		        String line;
		        boolean containsClass = false;
		        while ((line = reader.readLine()) != null) {
		          if (line.contains("class ")) {
		            containsClass = true;
		            break;
		          }
		        }
		        // Seçili dosyalarda analiz işlemlerinin yapıldığı yer
		        if (containsClass) {		        	
		        	satirAnaliz(file);
		        	}
		        } catch (IOException e) {
		        	System.err.println("Dosya okuma hatası: " + file.getName());
		        	}
		      } else if (file.isDirectory()) {
		      // Alt dizinleri recursive olarak işler
		      processSubfolders(file.getPath());  
		    }
		  }
		}

}
