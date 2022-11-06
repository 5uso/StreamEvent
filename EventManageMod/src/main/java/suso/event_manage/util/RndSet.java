package suso.event_manage.util;

import org.jetbrains.annotations.Nullable;

import java.util.*;

public class RndSet<T> extends AbstractSet<T> {
    private List<T> l = new LinkedList<>();
    private Map<T, Integer> m = new HashMap<>();
    private final Random r = new Random();

    public RndSet() {
    }

    public RndSet(Set<T> data) {
        for(T element : data) {
            l.add(element);
            m.put(element, m.size());
        }
    }

    public RndSet(Collection<T> data) {
        this.addAll(data);
    }

    @Override
    public int size() {
        return m.size();
    }

    @Override
    public boolean isEmpty() {
        return m.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return m.containsKey(o);
    }

    @Override
    public Iterator<T> iterator() {
        return l.iterator();
    }

    @Override
    public Object[] toArray() {
        return l.toArray();
    }

    @Override
    public boolean add(T element) {
        if(m.containsKey(element)) return false;

        l.add(element);
        m.put(element, m.size());
        return true;
    }

    @Override
    public boolean remove(Object element) {
        if(!m.containsKey(element)) return false;

        l.remove((int) m.get(element));
        m.remove(element);
        return true;
    }

    @Override
    public void clear() {
        l = new LinkedList<>();
        m = new HashMap<>();
    }

    public T get(int i) {
        return l.get(i);
    }

    @Nullable
    public T getRandom() {
        if(l.size() == 0) return null;
        return l.get(r.nextInt(l.size()));
    }
}
