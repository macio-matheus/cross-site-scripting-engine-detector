package utilsLib.util;

import java.util.HashMap;

/**
 * Pool de mensagens.
 */
public class MessagePool {
    private static HashMap messages = new HashMap();

    public MessagePool() {
    }

    /**
     * Opera as mensagens globais, para que haja controle de concorrencia
     * acesso/escrita.
     *
     * @param key     Chave da mensagem que se deseja operar.
     * @param message Mensagem a ser atribuída a mesnagem, se for o caso.
     * @param op      0: put; 1: remove; qq: rmeove todos.
     *                setar o valor da chave com o valor de message.
     * @return        Se isGet, retorna o valor da chave passado.
     */
    protected synchronized Object operateMessages(Object key, Object message,
                                                  int op) {
        if (op == 0) {
            messages.put(key, message);
        } else if (op == 1) {
            return messages.remove(key);
        } else {
            messages.clear();
        }

        return null;
    }

    /**
     * Retorna a mensage cuja é "key".
     *
     * @param key Chave da mensage que se deseja retorna.
     * @return    Mensagem.
     */
    public Object get(Object key) {
        return messages.get(key);
    }

    /**
     * Seta o valor da mensagem, sendo key a chave de acesso.
     *
     * @param key     Chave da mensagem.
     * @param message Mensagem. Pode ser nulo.
     */
    public void set(Object key, Object message) {
        operateMessages(key, message, 0);
    }

    /**
     * Remove a mensagem cujo key é o bassado.
     *
     * @param key   Chave
     * @return      Objeto removido
     */
    public Object remove(Object key) {
        return operateMessages(key, null, 1);
    }

    /**
     *
     * @return chaves das mensagens existentes.
     */
    public Object[] getKeys() {
        return messages.keySet().toArray(new Object[messages.size()]);
    }

    public void removeAll() {
        operateMessages(null, null, 2);
    }
}
