package utilsLib.util;

import java.util.ArrayList;

/**
 * Insere itens num arraylist de forma ordenada.
 *
 * OBS-01: Deve-se cuidar de sincronia, se for o caso.
 * OBS-02: Faltam:
 *    - Um método para inserir um monte de elementos de uma vez. Isso vai
 *      tornar as coisas mais rápidas.
 *    - Ao invés de armazenar os dados internamente com ArrayList, usar um
 *      array de Comparable. Ou seja, fazer o trabalho que o arrayLIst faz mas
 *      sem ficar fazendo os testes. Mas isso não vai melhorar muita coisa.
 */
public class OrderedArrayList {
    private ArrayList al;

    public OrderedArrayList() {
        al = new ArrayList();
    }

    /**
     * Pega o closerIndexOf e coloca o novo item na posição indicada.
     *
     *
     * @param item Comparable
     */
    public void add(Comparable item) {
        int closerIndex = this.closerIndexOf(item);

        if (closerIndex == this.size()) {
            al.add(this.size(), item);
        } else {
            // se for para colocar na posição -1, coloca na zero.
            // caso contrario, coloca na posição imediatamente depois
            //    do item antes de "item" e antes do item depios.
            al.add(closerIndex + 1, item);
        }
    }

    public Object remove(int index) {
        return al.remove(index);
    }

    public Object[] toArray() {
        return al.toArray();
    }

    public Object[] toArray(Object[] objs) {
        return al.toArray(objs);
    }

    public int indexOf(Comparable item) {
        if (al.size() == 0) {
            return -1;
        }

        int index = -1;
        int left = 0;
        int right = al.size() - 1;

        if (this.get(left).equals(item)) {
            index = left;
        } else if (this.get(right).equals(item)) {
            index = right;
        } else if (this.get(left).compareTo(item) < 0 &&
                   this.get(right).compareTo(item) > 0) {
            while (true) {
                if (right - left < 4) {
                    for (int i = left; i < right + 1; i++) {
                        if (this.get(i).equals(item)) {
                            index = i;
                            break;
                        }
                    }

                    break;
                }

                int m = left + ((right - left) / 2);

                Comparable middleObj = this.get(m);

                if (item.compareTo(middleObj) < 0) {
                    right = m;
                } else if (item.compareTo(middleObj) > 0) {
                    left = m;
                } else {
                    index = m;
                    break;
                }
            }
        }

        return index;
    }

    /**
     * Retorna o índice mais próximo do item dado, imediatamente
     * antes. Ou, se houver igual, retorna o próprio.
     *
     * @param item Comparable
     * @return Retorna o índice anterior ao item imediatamente depois de "item".
     * Pode retornar -1 ou entao size() + 1, indicando que é para botar no final.
     * Ou seja, o índice que voltar é o índice que, se fosse inserir item,
     * é onde ficaria.
     */
    public int closerIndexOf(Comparable item) {
        if (al.size() == 0) {
            return -1;
        }

        int index = -1;
        int left = 0;
        int right = al.size() - 1;

        Comparable firstLeft = this.get(left);
        Comparable firstRight = this.get(right);

        // Se for igual ao item atual, retorna (left ou right)
        // Se for menor q o primeiro left, então retorna, no caso, -1
        // Se for maior q o último, retorna, no caso, size() + 1
        // Caso contrario, busca o danado.
        if (firstLeft.equals(item)) {
            index = left;
        } else if (firstRight.equals(item)) {
            index = right;
        } else if (item.compareTo(firstLeft) < 0) {
            return left - 1;
        } else if (item.compareTo(firstRight) > 0) {
            return right + 1;
        } else {
            while (true) {
                // Se for menor q três itens, vai na tora.
                if (right - left < 4) {
                    for (index = left; index < right + 1; index++) {
                        Comparable currObj = this.get(index);

                        if (currObj.equals(item)) {
                            break;
                        } else if (currObj.compareTo(item) > 0) {
                            index--;
                            break;
                        }
                    }

                    break;
                }

                // Pega o meio e ve se vai pegar o item deopis do meio ou antes.

                int m = left + ((right - left) / 2);

                Comparable middleObj = this.get(m);

                if (item.compareTo(middleObj) < 0) {
                    right = m;
                } else if (item.compareTo(middleObj) > 0) {
                    left = m;
                } else {
                    index = m;
                    break;
                }
            }
        }

        return index;
    }

    public Comparable get(int index) {
        return (Comparable) al.get(index);
    }

    public int size() {
        return al.size();
    }

    public void ensureCapacity(int size) {
        al.ensureCapacity(size);
    }
}
