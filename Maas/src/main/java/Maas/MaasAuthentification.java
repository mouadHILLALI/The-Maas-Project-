package Maas;

public interface MaasAuthentification {
    abstract <T> Object login (T t);
    abstract <T> void logout (T t);
    abstract <T> Object register (T t);
}
