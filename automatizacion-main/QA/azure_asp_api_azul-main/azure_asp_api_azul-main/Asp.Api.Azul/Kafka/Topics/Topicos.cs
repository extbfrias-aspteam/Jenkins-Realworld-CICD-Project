namespace Asp.Api.Azul.Kafka.Topics
{
    public class Topicos
    {
        //ENTRADAS (CONSUMIDORES)
        //Spei In
        public const string SpeiInNotificationHandlerKafka = "SpeiInNotificationHandlerKafka";

        //Spei Out
        public const string SpeiOutNotificationHandlerKafka = "SpeiOutNotificationHandlerKafka";
        public const string SpeiOutPendienteHandlerKafka = "SpeiOutPendienteHandlerKafka";
        public const string SpeiOutRechazadoHandlerKafka = "SpeiOutRechazadoHandlerKafka";
        public const string SpeiOutReintentoHandlerKafka = "SpeiOutReintentoHandlerKafka";

        //Retorno
        public const string SpeiRetornoAzulApiHandlerKafka = "SpeiRetornoAzulApiHandlerKafka";
        public const string SpeiRetornoEstadoHandlerKafka = "SpeiRetornoEstadoHandlerKafka";

        //Prevencion de fraudes eiyu
        public const string PrevFraude_SolicitudValidada= "Prevfraude.solicitud.validada.handler";


        //SALIDAS (PRODUCTORES)
        //Asp Api Azul -> Motor Pagos
        public const string SpeiOutSies = "SendSpeiOutSIESHandlerKafka";
        public const string SpeiOutAsp = "SendSpeiOutASPHandlerKafka";
        public const string TraspasoEiyuToAsp = "SendSpeiTraspasoASPHandlerKafka";
        public const string RetornoSIES = "SendSpeiRetornoSIESHandlerKafka";
        public const string RetornoEiyuToAsp = "SendSpeiRetornoASPHandlerKafka";

        //Asp Api Azul -> Asp Notification
        public const string TraspasoCambioEstadoHandlerKafka = "TraspasoCambioEstadoHandlerKafka";
        public const string TraspasoEiyu = "TraspasoEiyuHandlerKafka";
        public const string TopicTraspasoCoreCambioEstadoHandlerKafka = "TraspasoCoreCambioEstadoHandlerKafka";

        //Asp Api Azul -> Prevencion de Fraudes EIYU
        public const string PrevFraude_SolicitudIniciada = "Prevfraude.solicitud.iniciada.handler";


    }
}
