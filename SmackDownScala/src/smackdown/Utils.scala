package smackdown

import scala.reflect.Manifest

object Utils {
  implicit def simpleIsAs(any: Any) = new {
    /** Equivalent to Any.isInstanceOf[T] */
    def is[T](implicit m: Manifest[T]) = m.erasure.isInstance(any)
    /** Equivalent to Any.asInstanceOf[T] */
    def as[T] = any.asInstanceOf[T]
  }
  implicit def listOfType[T](list: List[T]) = new {
    /** Filters and casts elements of list by the given element sub-type */
    def ofType[U](implicit m: Manifest[U]) = list.filter(_.is[U]).map(_.as[U])
    /** Filters elements of list by the given element sub-type */
    def filterType[U](implicit m: Manifest[U]) = list.filter(_.is[U])
    /** Casts elements of list by the given element sub-type */
    def cast[U](implicit m: Manifest[U]) = list.map(_.as[U])
  }
  implicit def setOfType[T](set: Set[T]) = new {
    /** Filters and casts elements of set by the given element sub-type */
    def ofType[U](implicit m: Manifest[U]) = set.filter(_.is[U]).map(_.as[U])
    /** Filters elements of set by the given element sub-type */
    def filterType[U](implicit m: Manifest[U]) = set.filter(_.is[U])
    /** Casts elements of list by the given element sub-type */
    def cast[U](implicit m: Manifest[U]) = set.map(_.as[U])
  }
}