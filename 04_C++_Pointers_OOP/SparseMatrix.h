// Name: Garcia Anthony John Abril
// Seat : 14
// Own work, code not shared
// class examples were used as reference
// thus may have similarities with others

#include "Element.h"

struct Node;
typedef Node* NodePtr;

struct Node
{
	Element e;
	NodePtr next;
};


class SparseMatrix
{
	public:
		SparseMatrix();
		~SparseMatrix();
		SparseMatrix(const SparseMatrix&);

		NodePtr Head(){return head;}
		void insertElement(Element);
		bool deleteElement(Element);
		void printMatrix(int[MAXROW][MAXCOL])const;
		int findValue(Element);
		bool exist(Element);
		void Sort(SparseMatrix&);

		void operator+=(SparseMatrix&);
		void operator-=(SparseMatrix&);
		void operator*=(SparseMatrix&);

	private:
		NodePtr head;
};