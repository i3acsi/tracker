package com.gasevskyV.tracker;

class StubInput implements Input {
    /**
     * Поле value содержит послел\довательность
     * ответоов пользователя. Например для добавлния новой заявки
     * первое значение  последовательности - 0,
     * далее название заявки - task name
     * и описание заявки - description.
     * Последовательность {"0", "task name", "description"}
     */
    private final String[] value;

    /**
     * Счетчик вызовов метода ask
     */
    private int position;

    /**
     * Конструктор с инициализацией value
     * @param value
     */
    StubInput(String[] value) {
        this.value = value;
    }

    private boolean exist(int key, int[] range) {
        boolean result = false;
        for (int value: range) {
            if (value == key) {
                result = true;
                break;
            }
        }
        return result;
    }

    /**
     * При вызове метода ask - возвращается значение, которое хранится
     * по адресу value с нулевым индексом
     * и происходит инкремент счетчика, следовательно следующий вызов метода
     * ask вернет значение следующео элемента value
     * @param question
     */
    @Override
    public String ask(String question) {
        return this.value[this.position++];
    }

    @Override
    public int ask(String question, int[] range) {
        int key = Integer.valueOf(this.ask(question));
        if (!exist(key, range)) {
            throw new MenuOutException("Out of menu range");
		}
		return key;
    }
}
