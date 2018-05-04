#include <stdio.h>
#include <stdlib.h>
#include <string.h>

// 단순 연결 리스트의 노드 구조를 구조체로 정의
typedf struct ListNode {
	char data[4];
	struct ListNode*link;
} listNode;

// 리스트 시작을 나타내는 head 노드를 구조체로 정의
typedf struct {
	listNode*head;
}linkedList_h;

// 공백 연결 리스트를 생성하는 연산
linkedlist_h*createLinkedList_h(void) {
	linkedList_h*L;
	L = (linkedList_h*)malloc(sizeof(linkedList_h));
	L -> head = NULL; // 공백 리스트이므로 NULL로 설정
	return L;
}