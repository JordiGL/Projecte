//package gestor;
//
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.sql.Timestamp;
//import java.util.Date;
//import model.Usuari;
//
///**
// *
// * @author Jordi Gómez Lozano
// */
//public class GestorUsuari {
//
//    private Connection connexio = null;
//
//    public GestorUsuari(Connection connexio){
//        this.connexio = connexio;
//    }
//
//
//    public void inserir(Usuari usuari) throws GestorException  {
//        String insert = "INSERT INTO usuaris VALUES(?,?,?,?,?,?,?)";
//        String consulta = "SELECT email FROM usuaris WHERE email=?";
//        PreparedStatement stm = null;
//
//        try{
//            stm = connexio.prepareStatement(consulta);
//
//            stm.setString(1, usuari.getEmail()); //Afegim argument a la sentència sql.
//            ResultSet rs = stm.executeQuery(); //Executem la sentència.
//
//            if(rs.next()){ //Controlem el resultat de la sentència.
//
//                throw new GestorException("Aquest email ja existeix a la base de dades");
//
//            }else{
//
//                //Obtenim els diferents arguments que afegirem a la base de dades
//                Date date = new Date();
//                Timestamp timestamp = new Timestamp(date.getTime());
//                String nom = usuari.getNom();
//                String genere = usuari.getGenere();
//                String telefon = usuari.getTelefon();
//                String email = usuari.getEmail();
//                String clau = usuari.getClau();
//                int esAdministrador = usuari.getAdministrador();
//
//                stm = connexio.prepareStatement(insert);
//                //Afegim els arguments obtinguts a la sentència sql.
//                stm.setString(1, email);
//                stm.setString(2, nom);
//                stm.setString(3, genere);
//                stm.setString(4, telefon);   ;
//                stm.setString(5, clau);
//                stm.setInt(6, esAdministrador);
//                stm.setTimestamp(7, timestamp);
//
//                stm.executeUpdate(); //Executem la sentencia.
//            }
//
//        } catch (SQLException | GestorException ex) {
//
//            System.out.println(ex);
//
//        }finally{
//
//            try {
//                if(stm!=null && !stm.isClosed()){
//                    stm.close();
//                }
//            } catch (SQLException ex) {
//                throw new GestorException("Error intentant tancar una sentència");
//            }
//        }
//    }
//
//    public void modificar(String email, Usuari usuari) throws GestorException{
//        String consulta = "SELECT email FROM usuaris WHERE email=?";
//        String update = "UPDATE usuaris SET email=?, nom=?, genere=?, telefon=?, password=?, es_administrador=?, ultim_login=? WHERE email=?";
//        PreparedStatement stm = null;
//
//        //Fem la consulta de l'usuari
//        try{
//
//            stm = connexio.prepareStatement(consulta);
//            stm.setString(1, email);
//            ResultSet rs = stm.executeQuery();
//
//            if(rs.next()){
//
//                //Com que l'usuari existeix a la base de dades, procedim a borrar-lo.
//                stm = connexio.prepareStatement(update);
//
//                stm.setString(1, usuari.getEmail());
//                stm.setString(2, usuari.getNom());
//                stm.setString(3, usuari.getGenere());
//                stm.setString(4, usuari.getTelefon());
//                stm.setString(5, usuari.getClau());
//                stm.setInt(6, usuari.getAdministrador());
//                stm.setTimestamp(7, usuari.getUltimLogin());
//                stm.setString(8, email);
//                stm.executeUpdate();
//
//            }else{
//                throw new GestorException("Aquest email no correspon a cap canal de la base de dades");
//            }
//
//        } catch (SQLException ex) {
//
//            throw new GestorException(ex.getMessage());
//
//        }finally{
//
//            try {
//                if(stm!=null && !stm.isClosed()){
//                    stm.close();
//                }
//            } catch (SQLException ex) {
//                throw new GestorException("Error intentant tancar una sentència");
//            }
//        }
//    }
//
//    public void eliminar(String email) throws GestorException{
//        String consulta = "SELECT email FROM usuaris WHERE email=?";
//        String delete = "DELETE FROM usuaris WHERE email=?";
//        PreparedStatement stm = null;
//
//        //Fem la consulta de l'usuari
//        try{
//
//            stm = connexio.prepareStatement(consulta);
//            stm.setString(1, email);
//            ResultSet rs = stm.executeQuery();
//
//            if(rs.next()){
//
//                //Com que l'usuari existeix a la base de dades, procedim a borrar-lo.
//                stm = connexio.prepareStatement(delete);
//                stm.setString(1, email);
//                stm.executeUpdate();
//
//            }else{
//                throw new GestorException("Aquest email no correspon a cap canal de la base de dades");
//            }
//
//        } catch (SQLException ex) {
//
//            throw new GestorException("Error SQL");
//
//        }finally{
//
//            try {
//                if(stm!=null && !stm.isClosed()){
//                    stm.close();
//                }
//            } catch (SQLException ex) {
//                throw new GestorException("Error intentant tancar una sentència");
//            }
//        }
//
//    }
//
//    public Usuari obtenirUsuari(String emailUsuari) throws GestorException{
//        String consulta = "SELECT email, nom, genere, telefon, password, es_administrador, ultim_login "
//                + "FROM usuaris WHERE email LIKE ?";
//        PreparedStatement stm = null;
//
//        //Fem la consulta.
//        try{
//            stm = connexio.prepareStatement(consulta);
//            stm.setString(1, emailUsuari);
//            ResultSet rs = stm.executeQuery();
//
//            while(rs.next()){
//                //Obtenim el usuari.
//                String email = rs.getString(1);
//                String nom = rs.getString(2);
//                String genere = rs.getString(3);
//                String telefon = rs.getString(4);
//                String clau = rs.getString(5);
//                int esAdministrador = rs.getInt(6);
//                Timestamp ultimLogin = rs.getTimestamp(7);
//
//                Usuari usuari = new Usuari( email, nom, genere, telefon, clau, esAdministrador, ultimLogin);
//
//                return usuari;
//            }
//
//            return null;
//
//        } catch (SQLException ex) {
//
//            throw new GestorException(ex.getMessage());
//
//        }finally{
//
//            try {
//                if(stm!=null && !stm.isClosed()){
//                    stm.close();
//                }
//            } catch (SQLException ex) {
//                System.err.println("Error intentant tancar una sentència");
//            }
//        }
//    }
//
//    public boolean comprovarEmail(String email) throws GestorException{
//        String consulta = "SELECT email FROM usuaris WHERE email=?";
//        PreparedStatement stm = null;
//
//        //Fem la consulta de l'usuari
//        try{
//
//            stm = connexio.prepareStatement(consulta);
//            stm.setString(1, email);
//            ResultSet rs = stm.executeQuery();
//
//            if(rs.next()){
//
//                return true;
//
//            }else{
//                return false;
//            }
//
//        } catch (SQLException ex) {
//
//            throw new GestorException("Error SQL");
//
//        }finally{
//
//            try {
//                if(stm!=null && !stm.isClosed()){
//                    stm.close();
//                }
//            } catch (SQLException ex) {
//                throw new GestorException("Error intentant tancar una sentència");
//            }
//        }
//    }
//}

