import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

public class savedObj implements Serializable{
	 /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public File selectedFile_S;
	 public  ArrayList<Line> WList_saved = new ArrayList<Line>();
	 public  ArrayList<AP> APs_saved=new ArrayList<AP>();
	 //public  ArrayList<AP> APshow_saved=new ArrayList<AP>();
	 public ArrayList<Spot> Spots_saved	 = new ArrayList<Spot>() ;
	 public float gridDist; 
	 public float gridDistP;
	 public int gw_saved;
	 public int cGW_saved;
	 public float currentY_saved;

	savedObj(File _file,ArrayList<Line> _walls, ArrayList<AP> _APs,ArrayList<Spot> _Spots,float _gridDist,float _gridDistP,int _gw,float _currentY,int _cgw){
		selectedFile_S = _file;
		WList_saved = _walls;
		APs_saved = _APs;
		Spots_saved = _Spots;
		gridDist = _gridDist;
		gridDistP = _gridDistP;
		gw_saved = _gw;
		cGW_saved = _cgw;
		currentY_saved = _currentY;
	//	APshow_saved = _APshow;
	}

}
