namespace Asp.Api.Azul.Helpers
{
	[System.AttributeUsage(System.AttributeTargets.Property)]
	public class DbColumnAttribute : System.Attribute
	{
		private string ColumnName;
		private int Size;
        private bool Pk;

        public DbColumnAttribute(string columnName, int size, bool pk)
		{
			ColumnName = columnName;
			Size = size;
			Pk = pk;
		}

        public DbColumnAttribute(string columnName, int size)
        {
            ColumnName = columnName;
            Size = size;
            Pk = false;
        }

        public DbColumnAttribute(string columnName, bool pk)
        {
            ColumnName = columnName;
            Size = 1;
            Pk = pk;
        }

        public DbColumnAttribute(string columnName)
        {
            ColumnName = columnName;
            Size = 1;
            Pk = false;
        }

        public string GetColumnName() => ColumnName;
		public int GetSize() => Size;
        public bool GetPk() => Pk;
    }
}
