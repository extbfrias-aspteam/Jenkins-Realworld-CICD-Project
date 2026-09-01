package net.cero.ahorro.common;

public class QueriesUtils {
    public static int MOVIMIENTO_retTraspaso= 16;
    public static int MOVIMIENTO_depTraspaso= 17;
    public static int MOVIMIENTO_depSpei= 33;
    public static int MOVIMIENTO_retSpei= 34;
    public static int MOVIMIENTO_devSpei= 37;
    public static int MOVIMIENTO_retCodi= 64;
    public static int MOVIMIENTO_depCodi= 65;
    public static int MOVIMIENTO_retSpeiCodi= 66;
    public static int MOVIMIENTO_depSpeiCodi= 67;
    public static int MOVIMIENTO_retPagoServicio= 69;
    public static int MOVIMIENTO_retRecargaTel= 71;
    public static int MOVIMIENTO_traRecibido= 23;
    public static final String CUENTA_NCPOSICIONGLOBLA = "n.cuentaah";
    public static final String ID_PERSONA_NCPOSICIONGLOBLA = " n.idpersona";
    public static String QUERY_CONSULTA_CUENTA_ASP = "SELECT tipocuentaah as tipo_cuenta, \n" +
                                                            "cuentaah, estatusah \n" +
                                                    "FROM nucleocentral.ncposicionglobalah n\n"+
                                                    "WHERE %s = ?\n";

    public static String QUERY_CONSULTA_MOVIMIENTOS_CUENTAS = " SELECT mc.cuenta ,\n" +
                                                                    " mc.monto::numeric(20, 2), \n" +
                                                                    " TO_CHAR(mc.fecha_creacion," +
                                                                    "'YYYY-MM-DD HH24=MI=SS') as fecha_movimiento,\n" +
                                                                    " mc.fecha as fecha_aplicacion,\n" +
                                                                    " mc.obs as descripcion, \n" +
                                                                    " coalesce(am.titulo, '') as titulo, \n" +
                                                                    " am.operacion, \n" +
                                                                    " am.movimiento_id\n" +
                                                                " FROM movimientos_caja mc \n" +
                                                                " LEFT JOIN ahorro_movimientos am \n" +
                                                                " \ton am.movimiento_id = mc.tipo_mov_id \n" +
                                                                " \tWHERE mc.cuenta = ?" +
                                                                " and mc.fecha_creacion between ? and ?";

    public static String QUERY_CONSULTA_MOVIMIENTOS_CUENTAS_CERO = "select coalesce(aht.id_spei, 0) as id_spei, \n" +
                                                                            "aht.monto, aht.descripcion,\n" +
                                                                            "coalesce(tt.titulo, '') as titulo, tt.operacion, \n" +
                                                                            "coalesce(tt.clave_trans_dock, '') as clave_trans_dock,\n" +
                                                                            "coalesce(aht.clave_rastreo, '') as clave_rastreo,\n" +
                                                                            "TO_CHAR(aht.fecha,'YYYY-MM-DD HH24:MI:SS') as fecha_aplicacion,\n" +
                                                                            "TO_CHAR(aht.fecha_creacion,'YYYY-MM-DD HH24:MI:SS') as fecha_movimiento,\n" +
                                                                            "aht.tipo_transaccion_id as movimiento_id\n" +
                                                                    "from ahorro.ahtransacciones_cuentas aht \n" +
                                                                    "left join ahorro.ahtipos_transacciones tt on tt.id = aht.tipo_transaccion_id  \n" +
                                                                    "left join ahorro.ahcuentas c on c.id = aht.cuenta_id \n" +
                                                                    "where c.cuenta = ? and aht.fecha_creacion between ? and ?";

    public static String QUERY_CONSULTA_DETALLE_MOVIMIENTO_CUENTA_INCOMMING = "SELECT si.id_spei_incoming , \n" +
                                                                            "si.nombre_ordenante , si.cuenta_ordenante  ,\n" +
                                                                            "si.nombre_beneficiario , si.cuenta_beneficiario  ,\n" +
                                                                            "coalesce (sc.url, '') as cep,\n" +
                                                                            "si.concepto_pago , \n" +
                                                                            "coalesce(si.referencia_cobranza, '') as referencia_cobranza, \n" +
                                                                            "si.cve_rastreo \n" +
                                                                    "FROM spei_incoming si \n" +
                                                                    "LEFT JOIN spei_cda sc on sc.id_spei = si.id_spei_incoming where si.cve_rastreo = ?";

    public static String QUERY_CONSULTA_DETALLE_MOVIMIENTO_CUENTA_OUTGOING = "SELECT so.id_spei_outgoing, \n" +
                                                                                    "so.nombre_ordenante, so.cuenta_ordenante, \n" +
                                                                                    "so.nombre_beneficiario, so.cuenta_beneficiario, \n" +
                                                                                    "coalesce(sc.url, '') as cep, \n" +
                                                                                    "so.concepto_pago, \n" +
                                                                                    "so.referencia as referencia_cobranza ,\n" +
                                                                                    "so.clave_rastreo as cve_rastreo\n" +
                                                                            "FROM spei_outgoing so \n" +
                                                                            "LEFT JOIN spei_cda sc on sc.id_spei = so.id_spei_outgoing where so.clave_rastreo = ?";
    public static String QUERY_CONSULTA_SOLICITANTE_BY_NUMERO ="SELECT dt.telefono, " +
                                                                        "ct.descripcion , " +
                                                                        "dt.observaciones, " +
                                                                        "dt.id_solicitante  \n" +
                                                                "FROM directorio_telefonico dt\n" +
                                                                "LEFT JOIN cat_telefonos ct on ct.id_cat_telefono = dt.id_cat_telefono\n" +
                                                                "where dt.telefono = ? and dt.id_cat_telefono = 7";
}
