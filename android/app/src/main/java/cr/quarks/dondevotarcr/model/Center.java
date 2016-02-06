package cr.quarks.dondevotarcr.model;

import com.suigeneris.android.core.bo.BaseBusinessObject;
import com.suigeneris.android.core.data.DatabaseField;
import com.suigeneris.android.core.data.DatabaseTable;

/**
 * Created by altfuns on 2/5/16.
 */
@DatabaseTable(name = "centros")
public class Center extends BaseBusinessObject{
    @DatabaseField(name = "tipo")
    private String type;
    @DatabaseField(name = "nombre")
    private String name;
    @DatabaseField(name = "direccion")
    private String address;
    @DatabaseField(name = "url")
    private String url;
    @DatabaseField(name = "distrito_electoral")
    private String electoralDistrict;
    @DatabaseField(name = "distrito")
    private String district;
    @DatabaseField(name = "canton")
    private String canton;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getElectoralDistrict() {
        return electoralDistrict;
    }

    public void setElectoralDistrict(String electoralDistrict) {
        this.electoralDistrict = electoralDistrict;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getCanton() {
        return canton;
    }

    public void setCanton(String canton) {
        this.canton = canton;
    }

    /**
     * Find the center by the junta id
     * @param juntaId
     * @return
     */
    public static Center findByJunta(int juntaId){
        String query = "SELECT c.*" +
                ", de.nombre AS distrito_electoral" +
                ", d.nombre as distrito " +
                ", d.nombre as distrito " +
                ", ca.nombre as canton " +
                "FROM centros c " +
                "INNER JOIN juntas j ON c._id = j.centro_id " +
                "INNER JOIN distritos_electorales de ON c.distrito_electoral_id = de._id " +
                "INNER JOIN distritos d ON de.distrito_id = d._id " +
                "INNER JOIN cantones ca ON d.canton_id = ca._id " +
                "WHERE j._id = ?";
        return findSqlFirst(Center.class, query, new String[]{String.valueOf(juntaId)});

    }
}
