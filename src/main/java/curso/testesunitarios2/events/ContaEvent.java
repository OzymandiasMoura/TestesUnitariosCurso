package curso.testesunitarios2.events;

import curso.testesunitarios2.dominio.Conta;

public interface ContaEvent
{
    enum EventType
    {
        CREATED, UPDATED, DELETED
    }

    void dispatch(Conta conta, EventType eventType);
}
