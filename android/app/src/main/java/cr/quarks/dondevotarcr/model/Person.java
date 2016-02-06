package cr.quarks.dondevotarcr.model;

import com.suigeneris.android.core.bo.BaseBusinessObject;
import com.suigeneris.android.core.data.DatabaseField;
import com.suigeneris.android.core.data.DatabaseTable;

/**
 * Created by altfuns on 2/4/16.
 */
@DatabaseTable(name = "personas")
public class Person extends BaseBusinessObject {

    public static final String IDENTIFICATION_COLUMN = "cedula";

    @DatabaseField(name = "codelec")
    private String codeElec;
    @DatabaseField(name = IDENTIFICATION_COLUMN)
    private String identification;
    @DatabaseField(name = "expiracion")
    private String expiration;
    @DatabaseField(name = "nombre")
    private String name;
    @DatabaseField(name = "apellido_1")
    private String lastName1;
    @DatabaseField(name = "apellido_2")
    private String lastName2;
    @DatabaseField(name = "genero")
    private int gender;
    @DatabaseField(name = "junta_id")
    private int juntaId;


    public String getIdentification() {
        return identification;
    }

    public void setIdentification(String identification) {
        this.identification = identification;
    }

    public String getExpiration() {
        return expiration;
    }

    public void setExpiration(String expiration) {
        this.expiration = expiration;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName1() {
        return lastName1;
    }

    public void setLastName1(String lastName1) {
        this.lastName1 = lastName1;
    }

    public String getLastName2() {
        return lastName2;
    }

    public void setLastName2(String lastName2) {
        this.lastName2 = lastName2;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public int getJuntaId() {
        return juntaId;
    }

    public void setJuntaId(int juntaId) {
        this.juntaId = juntaId;
    }

    @Override
    public String toString() {
        return name + " " + lastName1 + " " + lastName2;
    }

    /**
     * Find the person by its identification
     * @param identification
     * @return
     */
    public static Person findByIdentification(String identification){
        return findFirst(Person.class, String.format(" %s = ?", IDENTIFICATION_COLUMN), new String[]{identification});
    }
    /**
     * Creates a new person from a CSV line
     *
     * -----------------------------------------------------------------------
     | CAMPO      | LARGO | DE    A | SIGNIFICADO                          |
     |------------|-------|---------|--------------------------------------|
     | CEDULA     |   9   |  1    9 | # cÈdula del ciudadano               |
     | CODELEC    |   6   | 10   15 | CÛdigo Electoral donde est· inscrito |
     | SEXO       |   1   | 16   16 | 1=Masculino   2=Femenino             |
     | FECHACADUC |   8   | 17   24 | Fecha vencimiento de la cÈdula       |
     | JUNTA      |   5   | 25   29 | # Junta Receptora de Votos           |
     | NOMBRE     |  30   | 30   59 | Nombre completo del ciudadano        |
     | 1.APELLIDO |  26   | 60   85 | Primer apellido                      |
     | 2.APELLIDO |  26   | 86  111 | Segundo apellido                     |
     -----------------------------------------------------------------------
     * @param line
     * @return
     */
    public static Person createPerson(String line){
        //String[] fields = line.split(",");
        Person person = new Person();
        person.identification = line.substring(0,9);
        person.codeElec = line.substring(10, 16);
        person.gender = Integer.parseInt(line.substring(17,18));
        person.expiration = line.substring(19, 27);
        person.juntaId = Integer.parseInt(line.substring(28, 33));
        person.name = line.substring(34, 64).trim();
        person.lastName1 = line.substring(65, 91).trim();
        person.lastName2 = line.substring(92, 118).trim();

//        person.identification = fields[0];
//        person.codeElec = fields[1];
//        person.gender = Integer.parseInt(fields[2]);
//        person.expiration = fields[3];
//        person.juntaId = Integer.parseInt(fields[4]);
//        person.name = fields[5].trim();
//        person.lastName1 = fields[6].trim();
//        person.lastName2 = fields[7].trim();
        return person;
    }
}
