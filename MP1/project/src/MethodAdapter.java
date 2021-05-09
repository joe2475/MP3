

import java.io.IOException;

import org.objectweb.asm.*;

public class MethodAdapter extends MethodVisitor implements Opcodes {
    public MethodAdapter(MethodVisitor mv) {
        super(ASM5,mv);
    }
    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
    	switch (opcode) {
        	case INVOKEVIRTUAL:
        		//check if it is "Thread.start()"
        		if(isThreadClass(owner)&&name.equals("start")&&desc.equals("()V")) {
	            	mv.visitInsn(DUP);
	        		mv.visitMethodInsn(INVOKESTATIC, "Log", "logStart",
	        				"(Ljava/lang/Thread;)V",false);
				}//check if it is "Thread.join()"
        		else if(isThreadClass(owner)&&name.equals("join")&&desc.equals("()V")) {
        			mv.visitInsn(DUP);
	        		mv.visitMethodInsn(INVOKESTATIC, "Log", "logJoin",
	        				"(Ljava/lang/Thread;)V",false);

    			} //check if it is "Object.wait()"
            	else if(name.equals("wait")&&
                				(desc.equals("()V")||desc.equals("(J)V")||desc.equals("(JI)V"))) {
            		mv.visitInsn(DUP);
	        		mv.visitMethodInsn(INVOKESTATIC, "Log", " logWait",
	        				"(Ljava/lang/Thread;)V",false);

        		} //check if it is "Object.notify()"
                else if(name.equals("notify")&&desc.equals("()V")) {
                	mv.visitInsn(DUP);
	        		mv.visitMethodInsn(INVOKESTATIC, "Log", "logNotify",
	        				"(Ljava/lang/Thread;)V",false);

            	}//check if it is "Object.notifyAll()"
                else if(name.equals("notifyAll")&&desc.equals("()V")) {
                	mv.visitInsn(DUP);
	        		mv.visitMethodInsn(INVOKESTATIC, "Log", "logNotifyAll",
	        				"(Ljava/lang/Thread;)V",false);

                				}
        	default: mv.visitMethodInsn(opcode, owner, name, desc,itf);
    	}

    }
    
    
    
    @Override
    public void visitFieldInsn(int opcode, String owner, String name, String desc) {
          switch (opcode) {
                case GETSTATIC:
    		//your code here
                    break;
                case PUTSTATIC:
                	//your code here
                    break;
                case GETFIELD:
                	//your code here
                    break;
                case PUTFIELD:
                	//your code here
    		//this part is slightly more complicated
                 default: break;
          	}
        mv.visitFieldInsn(opcode, owner, name, desc);
    }    
    
    @Override
    public void visitInsn(int opcode) {
    	boolean isSynchronized = false; 
    	boolean isStatic = false; 
    switch (opcode) {
    case Opcodes.MONITORENTER:
    	mv.visitInsn(Opcodes.DUP);
    	mv.visitMethodInsn(Opcodes.INVOKESTATIC, "Log",
    			"logLock","(Ljava/lang/Object;)V",false);
    break;
    case Opcodes.MONITOREXIT:
    	mv.visitInsn(Opcodes.DUP);
    	mv.visitMethodInsn(Opcodes.INVOKESTATIC, "Log",
    	"logUnlock","(Ljava/lang/Object;)V",false); 
    break;
    case Opcodes.IRETURN:
    case Opcodes.LRETURN:
    case Opcodes.FRETURN:
    case Opcodes.DRETURN:
    case Opcodes.ARETURN:
    case Opcodes.RETURN:
    case Opcodes.ATHROW:
    {
    	if(isSynchronized){
    		if(isStatic){
    			mv.visitInsn(Opcodes.ACONST_NULL);
    			mv.visitMethodInsn(Opcodes.INVOKESTATIC, "Log",
    					"logUnlock","(Ljava/lang/Object;)V",false);
    }
    		else{
    			mv.visitVarInsn(Opcodes.ALOAD, 0);
    			mv.visitMethodInsn(Opcodes.INVOKESTATIC, "Log",
    			"logUnlock","(Ljava/lang/Object;)V",false);
    }
    }
    	break; 
    }
    
    
    case AALOAD:case BALOAD:case CALOAD:case SALOAD:case IALOAD:case FALOAD:case DALOAD:case LALOAD:
        //your code here
        break;
    case AASTORE:case BASTORE:case CASTORE:case SASTORE:case IASTORE:case FASTORE:
        //your code here
        //this part is slightly more complicated
        break;
    case DASTORE:case LASTORE:
        //your code here
        //this part is slightly more complicated
        break;
    
    default:break;
    }
    mv.visitInsn(opcode);
    }
    
    
   /* @Override
    public void visitMaxs(int maxStack, int maxLocals) {
        mv.visitMaxs(X, Y); // set X and Y to a proper value
    }*/

    private boolean isThreadClass(String cname)
    {
    	while(!cname.equals("java/lang/Object"))
    	{
    		if(cname.equals("java/lang/Thread"))
    			return true;

    		try {
				ClassReader cr= new ClassReader(cname);
				cname = cr.getSuperName();
			} catch (IOException e) {
				return false;
			}
    	}
    	return false;
    }
}