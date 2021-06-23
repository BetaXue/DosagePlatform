package com.evision.dosage.pojo.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ExcelCellCoordinate {
    /**
     * 单元格行索引
     */
    private int row;
    /**
     * 单元格列索引
     */
    private int column;
    /**
     * 单元格的行数（合并单元格大于 1)
     */
    private int rowSize = 1;
    /**
     * 单元格的列数（合并单元格大于 1)
     */
    private int columnSize = 1;

    public void setCoordinate(int row, int column){
        this.row = row;
        this.column = column;
    }

    public void setCoordinate(int row, int column, int rowSize, int columnSize){
        this.row = row;
        this.column = column;
        this.rowSize = rowSize;
        this.columnSize = columnSize;
    }
}
