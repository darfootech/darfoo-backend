package com.darfoo.backend;

import com.darfoo.backend.service.cota.TypeClassMapping;
import org.junit.Test;

/**
 * Created by zjh on 15-4-2.
 */
public class EnumTests {
    @Test
    public void getEnumValueFromMetaData() {
        String[] fields = {"hot", "priority"};
        for (String field : fields) {
            //negative value
            Enum negativevalue = Enum.valueOf(TypeClassMapping.resourceFieldClassMap.get(field), String.format("NOT%s", field.toUpperCase()));
            System.out.println(negativevalue);
            //positive value
            Enum positivevalue = Enum.valueOf(TypeClassMapping.resourceFieldClassMap.get(field), String.format("IS%s", field.toUpperCase()));
            System.out.println(positivevalue);
        }
    }
}
