package Ada;

public class Magico {

	public static String magia(String[] deco) {
		 try {
			    Class<?> act = Class.forName("Acciones."+deco[0]);
			    try {
					act.getMethod("", deco.getClass()).invoke(deco);
				} catch (Exception e) {
					e.printStackTrace();
				}
			 } catch (ClassNotFoundException e) {
			        e.printStackTrace();
			}
		return null;
	}

}
