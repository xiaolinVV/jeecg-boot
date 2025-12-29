package org.jeecg.tools.codegen;

import java.util.List;
import java.util.Map;
import lombok.Data;

@Data
public class CodegenSpec {
    private String jspMode;
    private String projectPath;
    private String templatePath;
    private String bussiPackage;
    private String sourceRootPackage;
    private String webRootPackage;
    private String primaryKeyField;
    private String vueStyle;

    private TableSpec table;
    private List<ColumnSpec> columns;
    private List<SubTableSpec> subTables;

    @Data
    public static class TableSpec {
        private String tableName;
        private String entityPackage;
        private String entityName;
        private String ftlDescription;
        private String primaryKeyPolicy;
        private String sequenceCode;
        private Integer fieldRowNum;
        private Integer searchFieldNum;
        private Integer fieldRequiredNum;
        private Map<String, Object> extendParams;
    }

    @Data
    public static class SubTableSpec {
        private String tableName;
        private String entityPackage;
        private String entityName;
        private String ftlDescription;
        private String primaryKeyPolicy;
        private String sequenceCode;
        private String foreignRelationType;
        private List<String> foreignKeys;
        private List<String> foreignMainKeys;
        private List<ColumnSpec> columns;
        private List<ColumnSpec> originalColumns;
        private Map<String, Object> extendParams;
    }

    @Data
    public static class ColumnSpec {
        private String fieldDbName;
        private String fieldName;
        private String filedComment;
        private String fieldType;
        private String fieldDbType;
        private String charmaxLength;
        private String precision;
        private String scale;
        private String nullable;
        private String classType;
        private String classTypeRow;
        private String optionType;

        private Integer fieldLength;
        private String fieldHref;
        private String fieldValidType;
        private String fieldDefault;
        private String fieldShowType;
        private Integer fieldOrderNum;
        private String isKey;
        private String isShow;
        private String isShowList;
        private String isQuery;
        private String queryMode;
        private String dictField;
        private String dictTable;
        private String dictText;
        private String sort;
        private String readonly;
        private String defaultVal;
        private String uploadnum;
        private Map<String, Object> extendParams;
    }
}
