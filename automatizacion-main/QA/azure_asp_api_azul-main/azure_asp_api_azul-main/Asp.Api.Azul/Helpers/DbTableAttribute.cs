namespace Asp.Api.Azul.Helpers
{
    [System.AttributeUsage(System.AttributeTargets.Class)]
    public class DbTableAttribute : System.Attribute
    {
        private string TableName;

        public DbTableAttribute(string tableName)
        {
            TableName = tableName;
        }

        public string GetTableName() => TableName;
    }
}
