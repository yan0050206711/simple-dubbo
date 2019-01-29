package com.tstd2.soa.registry;

import java.util.List;

public interface NotifyListener {

    void notify(List<ServerNode> nodes);

}