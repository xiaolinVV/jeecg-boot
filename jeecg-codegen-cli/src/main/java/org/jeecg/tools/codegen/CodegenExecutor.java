package org.jeecg.tools.codegen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jeecg.common.constant.enums.CgformEnum;
import org.jeecgframework.codegenerate.generate.impl.CodeGenerateOne;
import org.jeecgframework.codegenerate.generate.impl.CodeGenerateOneToMany;
import org.jeecgframework.codegenerate.generate.pojo.ColumnVo;
import org.jeecgframework.codegenerate.generate.pojo.TableVo;
import org.jeecgframework.codegenerate.generate.pojo.onetomany.MainTableVo;
import org.jeecgframework.codegenerate.generate.pojo.onetomany.SubTableVo;

final class CodegenExecutor {
    private final CodegenSpec spec;

    CodegenExecutor(CodegenSpec spec) {
        this.spec = spec;
    }

    void run() throws Exception {
        validate();
        CgformEnum cgform = CgformEnum.getCgformEnumByConfig(spec.getJspMode());
        if (cgform == null) {
            throw new IllegalArgumentException("Unknown jspMode: " + spec.getJspMode());
        }

        String templatePath = spec.getTemplatePath() != null ? spec.getTemplatePath() : cgform.getTemplatePath();
        String stylePath = cgform.getStylePath();
        applyGlobalConfig(templatePath);

        if (cgform.getType() == 2) {
            generateOneToMany(templatePath, stylePath);
        } else {
            generateOne(templatePath, stylePath);
        }
    }

    private void validate() {
        if (spec.getProjectPath() == null || spec.getProjectPath().trim().isEmpty()) {
            throw new IllegalArgumentException("projectPath is required");
        }
        if (spec.getJspMode() == null || spec.getJspMode().trim().isEmpty()) {
            throw new IllegalArgumentException("jspMode is required");
        }
        if (spec.getTable() == null) {
            throw new IllegalArgumentException("table is required");
        }
        if (spec.getColumns() == null || spec.getColumns().isEmpty()) {
            throw new IllegalArgumentException("columns is required");
        }
        if (CgformEnum.getCgformEnumByConfig(spec.getJspMode()) != null
            && CgformEnum.getCgformEnumByConfig(spec.getJspMode()).getType() == 2) {
            if (spec.getSubTables() == null || spec.getSubTables().isEmpty()) {
                throw new IllegalArgumentException("subTables is required for onetomany modes");
            }
        }
    }

    private void applyGlobalConfig(String templatePath) {
        org.jeecgframework.codegenerate.a.a.a(spec.getProjectPath());
        org.jeecgframework.codegenerate.a.a.b(templatePath);
        if (spec.getBussiPackage() != null && !spec.getBussiPackage().trim().isEmpty()) {
            org.jeecgframework.codegenerate.a.a.g = spec.getBussiPackage().trim();
        }
        if (spec.getSourceRootPackage() != null && !spec.getSourceRootPackage().trim().isEmpty()) {
            org.jeecgframework.codegenerate.a.a.h = spec.getSourceRootPackage().trim();
        }
        if (spec.getWebRootPackage() != null && !spec.getWebRootPackage().trim().isEmpty()) {
            org.jeecgframework.codegenerate.a.a.i = spec.getWebRootPackage().trim();
        }
        if (spec.getPrimaryKeyField() != null && !spec.getPrimaryKeyField().trim().isEmpty()) {
            org.jeecgframework.codegenerate.a.a.l = spec.getPrimaryKeyField().trim();
        }
    }

    private void generateOne(String templatePath, String stylePath) throws Exception {
        TableVo tableVo = toTableVo(spec.getTable());
        List<ColumnVo> columns = toColumnVoList(spec.getColumns());
        List<ColumnVo> originalColumns = new ArrayList<>(columns);
        CodeGenerateOne generator = new CodeGenerateOne(tableVo, columns, originalColumns);
        generator.generateCodeFile(spec.getProjectPath(), templatePath, stylePath);
    }

    private void generateOneToMany(String templatePath, String stylePath) throws Exception {
        MainTableVo mainTableVo = toMainTableVo(spec.getTable());
        List<ColumnVo> mainColumns = toColumnVoList(spec.getColumns());
        List<ColumnVo> originalMainColumns = new ArrayList<>(mainColumns);
        List<SubTableVo> subTables = toSubTables(spec.getSubTables(), spec.getTable().getEntityPackage());
        CodeGenerateOneToMany generator = new CodeGenerateOneToMany(mainTableVo, mainColumns, originalMainColumns, subTables);
        generator.generateCodeFile(spec.getProjectPath(), templatePath, stylePath);
    }

    private TableVo toTableVo(CodegenSpec.TableSpec table) {
        TableVo vo = new TableVo();
        vo.setTableName(table.getTableName());
        vo.setEntityPackage(table.getEntityPackage());
        vo.setEntityName(table.getEntityName());
        vo.setFtlDescription(table.getFtlDescription());
        vo.setPrimaryKeyPolicy(table.getPrimaryKeyPolicy());
        vo.setSequenceCode(table.getSequenceCode());
        vo.setFieldRowNum(table.getFieldRowNum());
        vo.setSearchFieldNum(table.getSearchFieldNum());
        vo.setFieldRequiredNum(table.getFieldRequiredNum());
        vo.setExtendParams(mergeExtendParams(table.getExtendParams()));
        return vo;
    }

    private MainTableVo toMainTableVo(CodegenSpec.TableSpec table) {
        MainTableVo vo = new MainTableVo();
        vo.setTableName(table.getTableName());
        vo.setEntityPackage(table.getEntityPackage());
        vo.setEntityName(table.getEntityName());
        vo.setFtlDescription(table.getFtlDescription());
        vo.setPrimaryKeyPolicy(table.getPrimaryKeyPolicy());
        vo.setSequenceCode(table.getSequenceCode());
        vo.setFieldRowNum(table.getFieldRowNum());
        vo.setSearchFieldNum(table.getSearchFieldNum());
        vo.setFieldRequiredNum(table.getFieldRequiredNum());
        vo.setExtendParams(mergeExtendParams(table.getExtendParams()));
        return vo;
    }

    private Map<String, Object> mergeExtendParams(Map<String, Object> extendParams) {
        Map<String, Object> merged = new HashMap<>();
        if (extendParams != null) {
            merged.putAll(extendParams);
        }
        if (spec.getVueStyle() != null && !spec.getVueStyle().trim().isEmpty()) {
            merged.put("vueStyle", spec.getVueStyle().trim());
        }
        return merged.isEmpty() ? null : merged;
    }

    private List<ColumnVo> toColumnVoList(List<CodegenSpec.ColumnSpec> specs) {
        List<ColumnVo> columns = new ArrayList<>();
        for (CodegenSpec.ColumnSpec specColumn : specs) {
            columns.add(toColumnVo(specColumn));
        }
        return columns;
    }

    private ColumnVo toColumnVo(CodegenSpec.ColumnSpec specColumn) {
        ColumnVo vo = new ColumnVo();
        vo.setFieldDbName(specColumn.getFieldDbName());
        vo.setFieldName(specColumn.getFieldName());
        vo.setFiledComment(specColumn.getFiledComment());
        vo.setFieldType(specColumn.getFieldType());
        vo.setFieldDbType(specColumn.getFieldDbType());
        vo.setCharmaxLength(specColumn.getCharmaxLength());
        vo.setPrecision(specColumn.getPrecision());
        vo.setScale(specColumn.getScale());
        vo.setNullable(specColumn.getNullable());
        vo.setClassType(specColumn.getClassType());
        vo.setClassType_row(specColumn.getClassTypeRow());
        vo.setOptionType(specColumn.getOptionType());

        vo.setFieldLength(specColumn.getFieldLength());
        vo.setFieldHref(specColumn.getFieldHref());
        vo.setFieldValidType(specColumn.getFieldValidType());
        vo.setFieldDefault(specColumn.getFieldDefault());
        vo.setFieldShowType(specColumn.getFieldShowType());
        vo.setFieldOrderNum(specColumn.getFieldOrderNum());
        vo.setIsKey(specColumn.getIsKey());
        vo.setIsShow(specColumn.getIsShow());
        vo.setIsShowList(specColumn.getIsShowList());
        vo.setIsQuery(specColumn.getIsQuery());
        vo.setQueryMode(specColumn.getQueryMode());
        vo.setDictField(specColumn.getDictField());
        vo.setDictTable(specColumn.getDictTable());
        vo.setDictText(specColumn.getDictText());
        vo.setSort(specColumn.getSort());
        vo.setReadonly(specColumn.getReadonly());
        vo.setDefaultVal(specColumn.getDefaultVal());
        vo.setUploadnum(specColumn.getUploadnum());
        vo.setExtendParams(specColumn.getExtendParams());
        return vo;
    }

    private List<SubTableVo> toSubTables(List<CodegenSpec.SubTableSpec> subSpecs, String defaultEntityPackage) {
        List<SubTableVo> subTables = new ArrayList<>();
        for (CodegenSpec.SubTableSpec subSpec : subSpecs) {
            if (subSpec.getForeignKeys() == null || subSpec.getForeignKeys().isEmpty()) {
                throw new IllegalArgumentException("subTables.foreignKeys is required");
            }
            if (subSpec.getForeignMainKeys() == null || subSpec.getForeignMainKeys().isEmpty()) {
                throw new IllegalArgumentException("subTables.foreignMainKeys is required");
            }
            SubTableVo vo = new SubTableVo();
            vo.setTableName(subSpec.getTableName());
            vo.setEntityPackage(subSpec.getEntityPackage() != null ? subSpec.getEntityPackage() : defaultEntityPackage);
            vo.setEntityName(subSpec.getEntityName());
            vo.setFtlDescription(subSpec.getFtlDescription());
            vo.setPrimaryKeyPolicy(subSpec.getPrimaryKeyPolicy());
            vo.setSequenceCode(subSpec.getSequenceCode());
            vo.setForeignRelationType(subSpec.getForeignRelationType());
            vo.setForeignKeys(subSpec.getForeignKeys().toArray(new String[0]));
            vo.setForeignMainKeys(subSpec.getForeignMainKeys().toArray(new String[0]));
            vo.setExtendParams(subSpec.getExtendParams());

            List<ColumnVo> columns = toColumnVoList(subSpec.getColumns());
            vo.setColums(columns);
            if (subSpec.getOriginalColumns() != null && !subSpec.getOriginalColumns().isEmpty()) {
                vo.setOriginalColumns(toColumnVoList(subSpec.getOriginalColumns()));
            } else {
                vo.setOriginalColumns(new ArrayList<>(columns));
            }
            subTables.add(vo);
        }
        return subTables;
    }
}
