package cr.quarks.dondevotarcr.model;

import com.suigeneris.android.core.bo.DataAccessObject;
import com.suigeneris.android.core.data.DatabaseField;
import com.suigeneris.android.core.data.DatabaseTable;

/**
 * Created by altfuns on 2/3/16.
 */
@DatabaseTable(name = "juntas")
public class Junta extends DataAccessObject {

    @DatabaseField(name = "centro_id")
    private int centroId;
}
