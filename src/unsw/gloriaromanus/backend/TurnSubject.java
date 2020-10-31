package unsw.gloriaromanus.backend;

public interface TurnSubject {
    public void attach(TurnObserver o);
    public void detach(TurnObserver o);
    public void notifyobservers();
}
