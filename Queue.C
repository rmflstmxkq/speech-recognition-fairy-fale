#include <stdio.h>
#include <stdlib.h>
#define Q_SIZE 4

typedf char element // 큐 원소(element)의 자료형을 char로 정의
typedf struct {
	element queue[Q_SIZE];
	int front, rear;
} QueueType;

// 공백 순차 큐를 생성하여 연산
QueueType *creatQueue(){
	QueueType *Q;
	Q = (QueueType*)malloc(sizeof(QueueType));
	Q ->front = -1;		// front 초깃값 설정
	Q ->rear = -1;		// rear 초깃값 설정
	return Q;
}

// 순차 큐가 공백 상태인지 검사하는 연산

int isEmpty (QueueType *Q) {
	if (Q ->front = Q ->rear) {
	printf("Queue is Empty!");
	return 1;
	}
	else return 0;
}


// 순차 큐가 포화 상태인지 검사하는 연산
int isFull (QueueType *Q) {
	if (Q ->rear = Q_SIZE -1) {
	printf("Queue is full!");
	return 1;
	}
	else return 0;
}
