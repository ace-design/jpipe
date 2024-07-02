import { Event, TextEditor, TextEditorSelectionChangeEvent } from 'vscode';

export interface EventSubscriber<T>{
    update(data: T): void;
}

export class EventRunner<T>{
    private event: Event<T>;
    private event_subscribers: EventSubscriber<T>[];
    
    constructor(event: Event<T>){
        this.event = event;
        this.event_subscribers = [];
    }
    
    public addSubscribers(...subscribers: EventSubscriber<T>[]){
        subscribers.forEach((subscriber)=>{
            if(!this.event_subscribers.includes(subscriber)){
                this.event_subscribers.push(subscriber);
            }
        })
    }

    public listen(){
        this.event_subscribers.forEach((subscriber)=>{
            this.event.call(this, (e) => subscriber.update(e));
        });
    }
}

export class EventManager{
    private events: EventRunner<any>[];

    constructor(){
        this.events = [];
    }

    public register(event: EventRunner<any>, ...subscribers: EventSubscriber<any>[]){
        if(this.events.includes(event)){
            this.addToExisting(event, subscribers);
        }else{
            this.addNewEvent(event, subscribers);
        }
    }

    public listen(){
        this.events.forEach((event)=> {
            event.listen();
        });
    }

    private addToExisting(event: EventRunner<any>, subscribers: EventSubscriber<any>[]): void{
        let new_event = this.events[this.events.indexOf(event)];
        subscribers.forEach((subscriber) =>{
            new_event.addSubscribers(subscriber);
        })   
    }

    private addNewEvent(event: EventRunner<any>, subscribers: EventSubscriber<any>[]): void{
        subscribers.forEach((subscriber) =>{
            event.addSubscribers(subscriber);
        });
        this.events.push(event);  
    }
}

//improve if necessary
export function isTextEditor(value: unknown): value is TextEditor | undefined{
    if(value === undefined) return true;
    
    //checking if it is an object
    if(!(value && typeof value === 'object' && !Array.isArray(value))) return false;
    
    if(!Object.hasOwn(value, "document")) return false;
    if(!Object.hasOwn(value, "selection")) return false;
    if(!Object.hasOwn(value, "selections")) return false;
    if(!Object.hasOwn(value, "visibleRanges")) return false;
    if(!Object.hasOwn(value, "options")) return false;
    if(!Object.hasOwn(value, "viewColumn")) return false;
    if(!Object.hasOwn(value, "edit")) return false;
    if(!Object.hasOwn(value, "insertSnippet")) return false;
    if(!Object.hasOwn(value, "setDecorations")) return false;
    if(!Object.hasOwn(value, "revealRange")) return false;
    if(!Object.hasOwn(value, "show")) return false;
    if(!Object.hasOwn(value, "hide")) return false;



    return true;
}

//improve if necessary
export function isTextEditorSelectionChangeEvent(value: unknown): value is TextEditorSelectionChangeEvent{
    if(!(value && typeof value === 'object' && !Array.isArray(value))) return false;
    if(!Object.hasOwn(value, "textEditor")) return false;
    if(!Object.hasOwn(value, "selections")) return false;
    if(!Object.hasOwn(value, "kind")) return false;

    return true;
}