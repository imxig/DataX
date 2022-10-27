package com.alibaba.datax.core.transport.transformer.self;

import com.alibaba.datax.common.element.Column;
import com.alibaba.datax.common.element.Record;
import com.alibaba.datax.common.element.StringColumn;
import com.alibaba.datax.common.exception.DataXException;
import com.alibaba.datax.core.transport.transformer.TransformerErrorCode;
import com.alibaba.datax.core.util.AESUtil;
import com.alibaba.datax.core.util.SecretUtil;
import com.alibaba.datax.transformer.Transformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

/**
 * 自定义一个 DataX Transformer，用于特殊情况下的解密.
 * Created by guoxi.li on 22/10/27.
 */
public class DecryptTransformer extends Transformer {
    private static final Logger LOG = LoggerFactory.getLogger(DecryptTransformer.class);

    public DecryptTransformer() {
        setTransformerName("dx_decrypt");
    }

    /**
     * 实现对指定字段的解密处理
     *
     * @param record 记录
     * @param paras transformer 方法传入的参数
     * @return Record
     */
    @Override
    public Record evaluate(Record record, Object... paras) {
        int columnIndex;
        try {
            if(paras.length == 1){
                columnIndex = (Integer) paras[0];
            }else if(paras.length ==2){
                columnIndex = (Integer) paras[0];
            }else{
                throw new RuntimeException(getTransformerName() + " paras at most 2");
            }
        }catch (Exception e){
            throw DataXException.asDataXException(
                    TransformerErrorCode.TRANSFORMER_ILLEGAL_PARAMETER, "paras:" + Arrays.asList(paras).toString() + " => " + e.getMessage());
        }

        Column column = record.getColumn(columnIndex);
        try {
            String oriValue = column.asString();
            //如果字段为空，跳过，不进行解密操作
            if(oriValue == null){
                return record;
            }
            String secretKey = SecretUtil.getSecurityProperties().getProperty("decrypt.privateKey");
            if(secretKey == null || secretKey.trim().length() < 1){
                LOG.warn("指定的解密密钥 key={} 无效，不做解密处理", secretKey);
                return record;
            }
            String newValue = AESUtil.decrypt(oriValue, secretKey);
            record.setColumn(columnIndex, new StringColumn(newValue));
        }catch (Exception e){
            throw DataXException.asDataXException(TransformerErrorCode.TRANSFORMER_RUN_EXCEPTION, e.getMessage(),e);
        }
        return record;
    }
}
