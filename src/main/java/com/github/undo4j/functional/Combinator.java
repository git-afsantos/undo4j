package com.github.undo4j.functional;

import java.util.concurrent.Callable;

/**
 * Combinator
 * 
 * @author afs
 * @version 2013
 */

public final class Combinator {
	/**************************************************************************
	 * Constructors
	 **************************************************************************/

	/** Empty constructor of objects of class Combinator. */
	private Combinator() {
		throw new UnsupportedOperationException();
	}

	/**************************************************************************
	 * Public Methods
	 **************************************************************************/

	/** */
	public static <A, B, C> Function<A, C> compose(Function<A, B> f, Function<B, C> g) {
		return new ComposedFunction<A, B, C>(f, g);
	}

	/** */
	public static <A, B> Callable<B> compose(Callable<A> c, Function<A, B> f) {
		return new ComposedCallable<A, B>(c, f);
	}

	/** */
	public static <T> Callable<T> conditional(Predicate p, Callable<T> cThen, Callable<T> cElse) {
		return new ConditionalCallable<T>(p, cThen, cElse);
	}

	/** */
	public static <A, B> Function<A, B> conditional(Predicate p, Function<A, B> fThen, Function<A, B> fElse) {
		return new ConditionalFunction<A, B>(p, fThen, fElse);
	}

	/** */
	public static <A, B> Callable<B> conditional(Predicate p, A arg, Function<A, B> fThen, Function<A, B> fElse) {
		return new CallableFunction<A, B>(new ConditionalFunction<A, B>(p, fThen, fElse), arg);
	}

	/** */
	public static <A, B> Callable<B> toCallable(Function<A, B> f, A arg) {
		return new CallableFunction<A, B>(f, arg);
	}

	/**************************************************************************
	 * Private Methods
	 **************************************************************************/

	// ...
}
