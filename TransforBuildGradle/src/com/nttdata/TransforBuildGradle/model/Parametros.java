
package com.nttdata.TransforBuildGradle.model;

import com.nttdata.TransforBuildGradle.Util.TransforBuildGradleUtil;

public class Parametros {
    private int SO; // 1. win 2. mac
    private int version;//0. 1.0.0  1. 1.1.1
    
    public static TransforBuildGradleUtil util = new TransforBuildGradleUtil();

    public Parametros(int ver) {
        
        this.SO = util.obtenerSistemaOperativo();
        this.version=ver;
    }

    public int getSO() {
        return SO;
    }

    public void setSO(int SO) {
        this.SO = SO;
    }

    public String getVersion() {
        if (this.version==0) {
            return "1.0.0";
        }else{
            return "1.1.1";
        }
    }

    public void setVersion(int version) {
        this.version = version;
    }
    
    

    
    
    
}
