package com.auth.Controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import com.auth.service.impl.ReduceAbstract;

public class ReduceUtil {

	public static List<? extends ReduceAbstract> reduce(List<? extends ReduceAbstract> list, String... type) {
		for (ReduceAbstract reducible : list) {
			reducible.setType(type);
		}
		return new ArrayList<>(new HashSet<>(list));
	}
	
	
}
