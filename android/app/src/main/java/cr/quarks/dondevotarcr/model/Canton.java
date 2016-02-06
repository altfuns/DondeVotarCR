package cr.quarks.dondevotarcr.model;

import com.suigeneris.android.core.bo.BaseBusinessObject;
import com.suigeneris.android.core.data.DatabaseField;
import com.suigeneris.android.core.data.DatabaseTable;

import java.util.List;

/**
 * Created by luisa on 2/5/16.
 */
@DatabaseTable(name = "cantones")
public class Canton extends BaseBusinessObject {
    public static final String PROVINCE_ID_COLUMN = "provincia_id";

    @DatabaseField(name = "nombre")
    private String name;

    @DatabaseField(name = PROVINCE_ID_COLUMN)
    private String provinceId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(String provinceId) {
        this.provinceId = provinceId;
    }

    @Override
    public String toString() {
        return name;
    }

    /**
     * Find all the canton by province
     * @param provinceId
     * @return
     */
    public static List<Canton> findByProvince(long provinceId){
        return find(Canton.class, null, PROVINCE_ID_COLUMN + " = ?", new String[]{String.valueOf(provinceId)}, null, null, null, null);
    }
}
