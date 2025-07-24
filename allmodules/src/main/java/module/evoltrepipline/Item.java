/**
 * 
 */
package module.evoltrepipline;

/**
* @author YFQ
* @date 2019-07-02 13:25:19
* @version 1.0
* <p>Description:</p>
*/
public class Item {
	
    private String name;
    private String tip;
    
    public Item(String name,String tip){
        this.name = name;
        this.tip = tip;
    }

    public String getName() {
        return name;
    }

    public String getTip() {
        return tip;
    }

    @Override
    public String toString(){
        return name;
    }
}