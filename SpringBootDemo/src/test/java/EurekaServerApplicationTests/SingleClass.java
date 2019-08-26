package EurekaServerApplicationTests;

public class SingleClass {

	/**
	 * 采用内部类的方式来实现单例
	 */
	private static class LazyHolder{
		private static final SingleClass sc =new SingleClass();
	}
	
	private SingleClass(){
		if(LazyHolder.sc != null){
			throw new RuntimeException("不允许出现多个相同的实例");
		}
	};
	
	public static final SingleClass getInstance(){
		return LazyHolder.sc;
	}
}
