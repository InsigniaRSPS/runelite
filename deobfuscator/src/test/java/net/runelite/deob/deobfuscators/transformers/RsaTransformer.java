package net.runelite.deob.deobfuscators.transformers;

import net.runelite.asm.ClassFile;
import net.runelite.asm.ClassGroup;
import net.runelite.asm.Method;
import net.runelite.asm.attributes.Code;
import net.runelite.asm.attributes.code.Instruction;
import net.runelite.asm.attributes.code.Instructions;
import net.runelite.asm.attributes.code.instructions.InvokeSpecial;
import net.runelite.asm.attributes.code.instructions.LDC;
import net.runelite.deob.Transformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RsaTransformer implements Transformer {

    private static final Logger logger = LoggerFactory.getLogger(RsaTransformer.class);

    private static final String RSA_MODULUS = "94210824259843347324509385276594109263523823612210415282840685497179394322370180677069205378760490069724955139827325518162089726630921395369270393801925644637806226306156731189625154078707248525519618118185550146216513714101970726787284175941436804270501308516733103597242337227056455402809871503542425244523";

    private boolean done = false;

    @Override
    public void transform(ClassGroup group) {
        for (ClassFile cf : group.getClasses()) {
            for (Method method : cf.getMethods()) {
                transform(method);
            }
        }

        logger.info("Transformed: " + done);
    }

    private void transform(Method method) {
        if (!method.getName().equals("<clinit>")) {
            return;
        }

        Code code = method.getCode();

        if (code == null) {
            return;
        }

        Instructions ins = code.getInstructions();

        for (Instruction i : ins.getInstructions()) {
            if (i instanceof InvokeSpecial) {
                if (((InvokeSpecial) i).getMethod().getClazz().getName().equals("java/math/BigInteger")) {
                    int index = ins.getInstructions().indexOf(i);

                    Instruction arg1 = ins.getInstructions().get(index - 2);

                    if (!(arg1 instanceof LDC)) {
                        continue;
                    }

                    Object content = ((LDC) arg1).getConstant();

                    if (!(content instanceof String)) {
                        continue;
                    }

                    String number = ((String) content);

                    if (number.length() < 256) {
                        continue;
                    }

                    // replace the key with ours
                    ((LDC) arg1).setConstant(RSA_MODULUS);

                    // remove the bipush
                    ins.getInstructions().remove(index - 1);

                    // remove the radix param
                    ((InvokeSpecial) i).removeParameter(1);
                    done = true;
                    break;
                }
            }
        }
    }

}
