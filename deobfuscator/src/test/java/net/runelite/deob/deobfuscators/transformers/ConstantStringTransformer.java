package net.runelite.deob.deobfuscators.transformers;

import net.runelite.asm.ClassFile;
import net.runelite.asm.ClassGroup;
import net.runelite.asm.Method;
import net.runelite.asm.attributes.Code;
import net.runelite.asm.attributes.code.Instruction;
import net.runelite.asm.attributes.code.Instructions;
import net.runelite.asm.attributes.code.instructions.LDC;
import net.runelite.deob.Transformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConstantStringTransformer implements Transformer {

    private static final Logger logger = LoggerFactory.getLogger(ConstantStringTransformer.class);

    @Override
    public void transform(ClassGroup group) {
        for (ClassFile cf : group.getClasses()) {
            for (Method method : cf.getMethods()) {
                transform(method);
            }
        }
    }

    private void transform(Method method) {
        Code code = method.getCode();
        if (code == null) {
            return;
        }

        Instructions ins = code.getInstructions();
        for (Instruction i : ins.getInstructions()) {
            if (i instanceof LDC) {
                LDC ldc = ((LDC) i);
                replace(method, ldc, "oldschool", "pvphero");
                replace(method, ldc, "jagex_cl_oldschool_", "jagex_cl_pvphero_");
                replace(method, ldc, "runescape.com", "pvphero.com");
                replace(method, ldc, ".runescape.com", ".pvphero.com");
            }
        }
    }

    private void replace(Method method, LDC ldc, String find, String replace) {
        Object constant = ldc.getConstant();
        if (!(constant instanceof String)) {
            return;
        }
        if (constant.equals(find)) {
            ldc.setConstant(replace);
            logger.info("Found \"{}\" and replaced it with \"{}\" in {}.", find, replace, method);
        }
    }

}
