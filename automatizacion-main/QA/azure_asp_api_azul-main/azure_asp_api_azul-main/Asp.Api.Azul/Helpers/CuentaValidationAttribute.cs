namespace Asp.Api.Azul.Helpers
{
   
    [System.AttributeUsage(System.AttributeTargets.Property, AllowMultiple =true)]
    public class CuentaValidationAttribute : System.Attribute
    {
        private bool _obligatorioN1;
        private bool _obligatorioN2;
        private bool _obligatorioN3yN4;
        private bool _obligatorioPf;
        private bool _obligatorioPm;
        private bool _obligatorioRl;
        private bool _obligatorioA;
        private bool _obligatorioCampos;

        public CuentaValidationAttribute(bool obligatorioN1 = false, bool obligatorioN2 = false,
            bool obligatorioN3yN4 = false, bool obligatorioPf = false, bool obligatorioPm = false, 
            bool obligatorioRl = false, bool obligatorioA = false,bool obligatoriosCampos = false)
        {
            _obligatorioN1 = obligatorioN1;
            _obligatorioN2 = obligatorioN2;
            _obligatorioN3yN4 = obligatorioN3yN4;
            _obligatorioPf = obligatorioPf;
            _obligatorioPm = obligatorioPm;
            _obligatorioRl = obligatorioRl;
            _obligatorioA = obligatorioA;
            _obligatorioCampos = obligatoriosCampos;
        }

        public bool GetObligatorioN1() => _obligatorioN1;
        public bool GetObligatorioN2() => _obligatorioN2;
        public bool GetObligatorioN3yN4() => _obligatorioN3yN4;
        public bool GetObligatorioPf() => _obligatorioPf;
        public bool GetObligatorioPm() => _obligatorioPm;
         
        public bool GetObligatorioRl() => _obligatorioRl;
        public bool GetObligatorioA() => _obligatorioA;

        public bool GetObligatorioCampos() => _obligatorioCampos;

        public bool GetIsObligatorioSolicitante(int nivel, string tipoPersona, bool camposObligatorios)
        {
            if (camposObligatorios == true && _obligatorioCampos)
            {
                return true;
            }

            bool obligatorio = false;
            switch (nivel)
            {
                case 1:
                    if (_obligatorioN1) obligatorio = true;
                    break;
                case 2:
                    if (_obligatorioN1 || _obligatorioN2) obligatorio = true;
                    break;
                case 3:
                case 4:
                    if (_obligatorioN1 || _obligatorioN2 || _obligatorioN3yN4) obligatorio = true;
                    break;
            }

            switch (tipoPersona)
            {
                case "PF":
                    if ((_obligatorioPf || _obligatorioA) && obligatorio) return true;
                    break;
                case "PM":
                    if ((_obligatorioPm || _obligatorioA) && obligatorio) return true;
                    break;
            }

            return false;
        }
    }
}
