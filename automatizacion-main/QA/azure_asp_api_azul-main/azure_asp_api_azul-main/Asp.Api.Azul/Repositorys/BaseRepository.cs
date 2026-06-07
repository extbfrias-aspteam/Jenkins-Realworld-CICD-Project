using System.ComponentModel.DataAnnotations;
using Asp.Api.Azul.Entities.DataBase;
using Asp.Api.Azul.Helpers;
using Npgsql;
using NpgsqlTypes;

namespace Asp.Api.Azul.Repositorys
{
    public class BaseRepository
    {
        public void GetColumns<T>(T table, out List<string> columns, out List<string> columnsArroba, out string pk)
        {
            columns = new List<string>();
            columnsArroba = new List<string>();
            var properties = typeof(T).GetProperties();
            pk = "";
            foreach (var property in properties)
            {
                var value = property.GetValue(table);
                if (value != null)
                {
                    var attr = property.GetCustomAttributes(true)
                        .FirstOrDefault(x => x.GetType() == typeof(DbColumnAttribute));
                    if (attr != null)
                    {
                        var attribute = attr as DbColumnAttribute;
                        if (attribute != null)
                        {
                            var columnName = attribute.GetColumnName();
                            if (attribute.GetPk())
                            {
                                pk = columnName;
                                continue;
                            }
                            columns.Add(columnName);
                            columnsArroba.Add($"@{columnName}");
                        }
                    }

                }
            }
        }

        public string GetQueryInsert<T>(T table)
        {

            //List<string> columns = new List<string>();
            //List<string> columnsArroba = new List<string>();
            //var properties = typeof(T).GetProperties();
            //var pk = "";
            //foreach (var property in properties)
            //{
            //    var value = property.GetValue(table);
            //    if (value != null)
            //    {
            //        var attr = property.GetCustomAttributes(true)
            //            .FirstOrDefault(x => x.GetType() == typeof(DbColumnAttribute));
            //        if (attr != null)
            //        {
            //            var attribute = attr as DbColumnAttribute;
            //            if (attribute != null)
            //            {
            //                var columnName = attribute.GetColumnName();
            //                if (attribute.GetPk())
            //                {
            //                    pk = columnName;
            //                    continue;
            //                }
            //                columns.Add(columnName);
            //                columnsArroba.Add($"@{columnName}");
            //            }
            //        }

            //    }
            //}
            GetColumns(table, out List<string> columns, out List<string> columnsArroba, out string pk);

            DbTableAttribute attrTable =
                (DbTableAttribute)Attribute.GetCustomAttribute(typeof(T), typeof(DbTableAttribute));

            //var attrsTable = table.GetType().GetCustomAttributes(typeof(T), true);
            //var attrTableO = attrsTable.FirstOrDefault(x => x.GetType() == typeof(DbTableAttribute));
            //var attrTable = attrTableO as DbTableAttribute;
            if (attrTable == null)
                throw new ValidationException("Nombre de tabla no especificado");

            var tableName = attrTable.GetTableName();

            var query = "INSERT INTO " + tableName + "(" +
                        string.Join(", ", columns) +
                        ") VALUES (" +
                        string.Join(", ", columnsArroba) +
                        ")";
            query += string.IsNullOrEmpty(pk)
                ? ""
                : $" RETURNING {pk}";
            return query;
        }

        public string GetQueryUpdate<T>(T table, string condition)
        {
            GetColumns(table, out List<string> columns, out List<string> columnsArroba, out string pk);

            DbTableAttribute attrTable =
               (DbTableAttribute)Attribute.GetCustomAttribute(typeof(T), typeof(DbTableAttribute));

            if (attrTable == null)
                throw new ValidationException("Nombre de tabla no especificado");

            var tableName = attrTable.GetTableName();

            List<string> columnsSet = new List<string>();

            for (var i = 0; i < columns.Count; i++)
            {
                columnsSet.Add($"{columns[i]} = {columnsArroba[i]}");
            }

            var query = $"UPDATE {tableName} SET {string.Join(", ", columnsSet)} WHERE {condition} RETURNING {pk}";

            return query;
        }

        public NpgsqlParameter[] GetParametersInsert<T>(T table)
        {
            List<NpgsqlParameter> parameters = new List<NpgsqlParameter>();
            var properties = typeof(T).GetProperties();
            foreach (var property in properties)
            {
                var value = property.GetValue(table);
                if (value != null)
                {
                    var attr = property.GetCustomAttributes(true)
                        .FirstOrDefault(x => x.GetType() == typeof(DbColumnAttribute));
                    if (attr != null)
                    {
                        var attribute = attr as DbColumnAttribute;
                        if (attribute != null)
                        {
                            var columnName = attribute.GetColumnName();
                            if (attribute.GetPk()) continue;
                            var paramType = NpgsqlDbType.Varchar;
                            var parameter = new NpgsqlParameter(columnName, value);
                            //if (property.PropertyType == typeof(string))
                            //{
                            //    parameter.NpgsqlDbType = NpgsqlDbType.Varchar;
                            //    parameter.Size = attribute.GetSize();
                            //}
                            parameters.Add(parameter);
                        }
                    }

                }
            }

            return parameters.ToArray();
        }
    }
}
