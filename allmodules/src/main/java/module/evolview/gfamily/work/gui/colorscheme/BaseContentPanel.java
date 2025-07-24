package module.evolview.gfamily.work.gui.colorscheme;

import javax.swing.JPanel;
/**
 * DialogFrame类的构造函数的传入参数的基类</br>
 * 有一个executeRenderProcess()函数，为点击ok后调用的过程
 */

@SuppressWarnings("serial")
public abstract class BaseContentPanel extends JPanel {
	
	/**
	 * 点击OK按钮之后的事件，有异常的话，直接抛出，最后会以一个Dialog的形式展现！
	 * @throws Exception
	 */
	public abstract void executeRenderProcess() throws Exception;

}
