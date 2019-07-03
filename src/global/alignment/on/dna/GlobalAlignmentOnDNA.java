
package global.alignment.on.dna;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

 /*Created by Deniz YILDIZTEKIN
              06.06.2019
    
    */
public class GlobalAlignmentOnDNA {
 
   
    private static final float eslesmeSkoru = (float) 3.621354295;
    private static final float boslukSkoru=(float)  -1.832482334;
    private static final  float yanlisEslesmeSkoru = (float) -2.451795405;
    
    private static Set<Float> karsilastirmaDegerleri=new HashSet<Float>();
    private static ArrayList<String> arrayList=new ArrayList<String>();
    
    
    public static void boslukDegeriAta(String DNA1,String DNA2,float Sekans [][]){
    
    Sekans[0][0]=0;
    for (int i = 0; i < DNA1.length()+1; i++){

        Sekans[i][0] = i == 0 ? 0 : Sekans[i-1][0] + boslukSkoru; 
    }

    for (int j = 0; j < DNA2.length()+1; j++){

        Sekans[0][j] = j == 0 ? 0 : Sekans[0][j-1] + boslukSkoru;
    }
          
    }
    
    
    
   public static void SkorHesapla(String DNA1,String DNA2,float Sekans[][]){
       
    float maxScore;
    float karsilastirmaSonucu;
    
    for (int i = 1; i < DNA1.length() + 1; i++)
    {
        for (int j = 1; j < DNA2.length() + 1; j++)
        {
            float  scoreDiag=0;
                
            if (DNA1.charAt(j - 1) == DNA2.charAt(i - 1)){
                scoreDiag = Sekans[i - 1][j - 1] + eslesmeSkoru;
            }
            else{
                scoreDiag = Sekans[i - 1][j - 1] + yanlisEslesmeSkoru;
            }

            float scoreLeft = Sekans[i][j - 1] + boslukSkoru;
            float scoreUp =  Sekans[i - 1][j] + boslukSkoru;

            maxScore = maksimumBul(maksimumBul(scoreDiag, scoreLeft), scoreUp);
            
            Sekans[i][j] = maxScore;
        }
    }
    
    
    karsilastirmaSonucu=Sekans[DNA1.length()][DNA2.length()];
    
    
        karsilastirmaDegerleri.add(karsilastirmaSonucu);
        
   
   }
            
  public static float maksimumBul(float x,float y){
      
      if(x>y)
          return x;
      else
          return y;
      
  }
 
  public static void sekanslariYazdir(){
      System.out.println();
       System.out.println("En buyuk degerler: ");
       System.out.println();
   
       for(int i=1;i<=20;i++){
        System.out.print(i+". deger : ");
        Float max=Collections.max(karsilastirmaDegerleri);
        System.out.println(max);
        karsilastirmaDegerleri.remove(max);
       }
       
  }
 
  
    public static void main(String[] args) {
        
        
           String dnaBilgisi="test";
          try(Scanner scanner = new Scanner(new BufferedReader(new FileReader("15K_Sequence.fasta")))) {
                
            while (scanner.hasNextLine()) {
                
                dnaBilgisi = scanner.nextLine();
                
                if(dnaBilgisi.contains("A")){
                     dnaBilgisi=dnaBilgisi.replace(" ","");
                     
                 
                    arrayList.add(dnaBilgisi);
                   
                    
                }
            } 
        } catch (FileNotFoundException ex) {
            System.out.println("Dosya Bulunamadi...");
        }catch (IOException ex) {
            System.out.println("Dosya acilirken bir hata olustu....");
        }
          
          
         
          int coreCount=Runtime.getRuntime().availableProcessors();
         
          float Sekans [][]=new float[dnaBilgisi.length()+1][dnaBilgisi.length()+1];
         
          
            //For making serial
//          for(int i=0;i<arrayList.size();i++){
//                            for(int j=i+1;j<arrayList.size();j++){
//                                 
//                                boslukDegeriAta(arrayList.get(i),arrayList.get(j),Sekans);     
//                                SkorHesapla(arrayList.get(i),arrayList.get(j),Sekans);
//                            }
//          }


        
          

          Thread thread1=new Thread(new Runnable() {
                     
                     @Override
                     public void run() {
                         
                         System.out.println("1.thread calisiyor...");
                         
                       
                          for(int i=0;i<arrayList.size()/coreCount;i++){
                            for(int j=i+1;j<arrayList.size();j++){
                                  
                                boslukDegeriAta(arrayList.get(i),arrayList.get(j),Sekans);     
                                SkorHesapla(arrayList.get(i),arrayList.get(j),Sekans);
                     
                            }
                          }
                     }
                 });
          
           Thread thread2=new Thread(new Runnable() {
                     
                     @Override
                     public void run() {
                         System.out.println("2.thread calisiyor...");
                       
                          for(int i=arrayList.size()/coreCount;i<2*arrayList.size()/coreCount;i++){
                            for(int j=i+1;j<arrayList.size();j++){
                                
                                boslukDegeriAta(arrayList.get(i),arrayList.get(j),Sekans);    
                                SkorHesapla(arrayList.get(i),arrayList.get(j),Sekans);
                 
                                 
                            }
                          }
                     }
                 });
                   Thread thread3=new Thread(new Runnable() {
                     
                     @Override
                     public void run() {
                         System.out.println("3.thread calisiyor...");
                         
                          for(int i=2*arrayList.size()/coreCount;i<3*arrayList.size()/coreCount;i++){
                            for(int j=i+1;j<arrayList.size();j++){
                                 
                                    boslukDegeriAta(arrayList.get(i),arrayList.get(j),Sekans);     
                                SkorHesapla(arrayList.get(i),arrayList.get(j),Sekans);
               
            
                     }
                            }
                          }
                     
                 });
                    Thread thread4=new Thread(new Runnable() {
                  
                     @Override
                     public void run() {
                         System.out.println("4.thread calisiyor...");
                      
                          for(int i=3*arrayList.size()/coreCount;i<arrayList.size();i++){
                            for(int j=i+1;j<arrayList.size();j++){
                                
                                boslukDegeriAta(arrayList.get(i),arrayList.get(j),Sekans);     
                                SkorHesapla(arrayList.get(i),arrayList.get(j),Sekans);

                                }
                          }
                     }
                 });
                    
                thread1.start();
                thread2.start();
                thread3.start();
                thread4.start();
                
        try {
            thread1.join();
            thread2.join();
            thread3.join();
            thread4.join();
        } catch (InterruptedException ex) {
            Logger.getLogger(GlobalAlignmentOnDNA.class.getName()).log(Level.SEVERE, null, ex);
        }   
            sekanslariYazdir();
               
    }

        
        
     
    }
    

