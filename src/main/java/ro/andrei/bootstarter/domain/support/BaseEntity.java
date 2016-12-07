package ro.andrei.bootstarter.domain.support;

public abstract class BaseEntity extends AbstractEntity<Long>{

	private static final long serialVersionUID = 1L;

	public abstract void update(BaseEntity entity);
}