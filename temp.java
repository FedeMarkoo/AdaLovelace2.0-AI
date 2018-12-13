
public class AdaLovelace {
 
  public String responder(String texto) {
  	   
    String[] deco = decodificar(texto);
    
    
    
    return respuesta;
  }
  
  private String[] decodificar(String texto){
  	String regex = "el|la|las|los";
  	texto = texto.replaceAll(regex,"");
    
    return BD.decodificar(texto);
  }
  
  private class clase(String clase){
  	if(clase.equals(this.getClass()+""))
      return this;
    return siguiente.clase(clase);
  }
  
}

public class AccionBasica(){
 
  public static void decir(String texto){
   system.out.println(texto);
  }
  
  public static void caminar(int x, int y){
   system.out.println("x: "+x+"  y: "+y);
  } 
}


public class UltimaOpcion(){
 
  public String realizar(String texto){
  AccionBasica.decir("No entiendo lo que me estas pidiendo... \nEs un sinomimo de una accion ya registrada?");
    if(AccionBasica.escuchar().toLower().contains("si")){}
    else
    {
      AccionBasica.decir("Desea agregar el codigo?");
      if(AccionBasica.escuchar().toLower().contains("si")){
      	AccionBasica.decir("Ingreselo");
        String codigo = AccionBasica.escuchar();
      
        
      }
    
    }
  }
  
}
